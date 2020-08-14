package de.visualdigits.dotgraph.dsl.maven.dotgraph

import java.io.File
import java.nio.file.Paths

import de.visualdigits.dotgraph.core.`type`.CreateLegend
import de.visualdigits.dotgraph.core.entity.{DotColor, DotGraph, DotNode}
import de.visualdigits.dotgraph.dsl.maven.model.pom.Pom
import de.visualdigits.dotgraph.dsl.maven.model.pom.artifact.Artifact

import scala.collection.mutable

/**
 * Represents a hierarchical Maven project.
 *
 * @param pomFile The top level node file to consume.
 * @param name  The top level name.
 */
class PomGraph(
                val pomFile: File,
                name: String = "",
                m2RepoDirectory: String = "",
                filterPackages: Seq[String] = Seq(),
                rootPackages: Seq[String] = Seq(),
                analyzeDependencies: Boolean = false
              ) extends DotGraph(name) {

  val nodeCache: mutable.Map[String, Pom] = mutable.Map()

  if (name.isEmpty) {
    this.id = pomFile.getParentFile.getCanonicalFile.getName
  }

  val node: Pom = Pom(pomFile, m2RepoDirectory, None)
  val pomNodes: mutable.LinkedHashSet[Pom] = {
    setCycleColors(new DotColor("#ff0000"))
      .setDetermineTransitiveEdges(true)
      .setCreateLegend(CreateLegend.FULL)

    val pomNodes: mutable.LinkedHashSet[Pom] = node.dfs()

    // add nodes
    pomNodes
        .map(pom => {
          PomNode(this, pom.artifact.key, pom, m2RepoDirectory, bgColor = DotColor.YELLOW_LIGHT, analyzeDependencies = analyzeDependencies)
        }).foreach(pn => {
          pn.resolveDependencyPoms(this, m2RepoDirectory, filterPackages)
        })

    // add edges
    pomNodes.foreach(pom =>
      pom.childPoms.foreach(child =>
        addEdgeById(pom.artifact.key, child.artifact.key))
    )

    // determine external parent projects
    pomNodes.foreach(pom => {
      pom.parent.foreach(parent => {
        determineExternalParents(pom, parent)
      })
    })

    nodes.values.map(_.asInstanceOf[PomNode])
      .filter(n => rootPackages.isEmpty || rootPackages.exists(n.pom.artifact.groupId.startsWith(_)))
      .foreach(n => {
        n.bgColor = DotColor.YELLOW
        n.showAttributes()
      })

    determineGraphRoutes
//    markParentEdges(pomNodes)

    pomNodes
  }

  override def removeNodeById(id: String, doRemoveEdges: Boolean = true): Option[DotNode] = {
    val removedNode = super.removeNodeById(id, doRemoveEdges)
    removedNode.foreach(r => pomNodes.remove(r.asInstanceOf[PomNode].pom))
    removedNode
  }

  def getAllDependencies: Set[Artifact] = {
    pomNodes.flatMap(pom => {
      pom.dependencies.values
    }).toSet
  }

  private def markParentEdges(pomNodes: mutable.LinkedHashSet[Pom]): Unit = {
    pomNodes.foreach(pom => {
      pom.childPoms.foreach(child => {
        val childArtifact = child.artifact
        val pomArtifact = pom.artifact
        val edges = getEdgesById(childArtifact.toString, pomArtifact.toString)
        val parent = child.parent
        if (parent.nonEmpty && parent.get == pomArtifact) {
          edges.foreach(edge => {
            val e = edge.asInstanceOf[PomEdge]
            e.isParent = true
            e.attributes.setPenwidth(5.0)
          })
        }
      })
    })
  }

  private def determineExternalParents(pom: Pom, parentArtifact: Artifact): Unit = {
    var parentPom = nodeCache.getOrElse(parentArtifact.key, null)
    if (parentPom == null) {
      val pomFile = Paths.get(System.getProperty("user.home"), ".m2", "repository", parentArtifact.groupId.replace('.', '/'), parentArtifact.artifactId, parentArtifact.version, parentArtifact.artifactId + "-" + parentArtifact.version + ".node").toFile
      if (pomFile.exists()) {
        parentPom = Pom(pomFile, m2RepoDirectory, None)
        parentPom.childPoms.addOne(pom)
        nodeCache.addOne(parentArtifact.key, parentPom)
      }
    }
    if (parentPom != null) {
      val name = parentPom.artifact.toString
      addNode(PomNode(this, name, parentPom, m2RepoDirectory, isExternal = true, analyzeDependencies = analyzeDependencies))
      addEdgeById(pom.artifact.toString, name)
      parentPom.parent.foreach(parent => {
        determineExternalParents(parentPom, parent)
      })
    }
  }
}

object PomGraph {
  def apply(
             pomFile: File,
             name: String = "",
             m2RepoDirectory: String = "",
             filterPackages: Seq[String] = Seq(),
             rootPackages: Seq[String] = Seq(),
             analyzeDependencies: Boolean = false
           ) = new PomGraph(pomFile, name, m2RepoDirectory, filterPackages, rootPackages, analyzeDependencies)
}