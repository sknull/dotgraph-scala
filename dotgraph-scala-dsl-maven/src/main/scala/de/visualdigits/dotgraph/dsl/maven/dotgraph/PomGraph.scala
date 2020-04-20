package de.visualdigits.dotgraph.dsl.maven.dotgraph

import java.io.File
import java.nio.file.Paths

import de.visualdigits.dotgraph.core.`type`.CreateLegend
import de.visualdigits.dotgraph.core.entity.{DotColor, DotGraph, DotNode}
import de.visualdigits.dotgraph.dsl.maven.model.`type`.UpdateMode
import de.visualdigits.dotgraph.dsl.maven.model.{Artifact, Pom, PropertyValue}

import scala.collection.mutable

/**
 * Represents a hierarchical Maven project.
 *
 * @param pomFile The top level node file to consume.
 * @param name  The top level name.
 */
class PomGraph(val pomFile: File, name: String = "") extends DotGraph(name) {

  val nodeCache: mutable.Map[String, Pom] = mutable.Map()

  if (name.isEmpty) {
    this.id = pomFile.getParentFile.getCanonicalFile.getName
  }

  val node: Pom = Pom(pomFile)
  val pomNodes: mutable.LinkedHashSet[Pom] = {
    setCycleColors(new DotColor("#ff0000"))
      .setDetermineTransitiveEdges(true)
      .setCreateLegend(CreateLegend.FULL)

    val pomNodes: mutable.LinkedHashSet[Pom] = node.dfs()

    // add nodes
    pomNodes.foreach(pom => addNode(PomNode(this, pom.artifact.toString, pom)))

    // add edges
    pomNodes.foreach(pom => pom.childPoms.foreach(child => addEdgeById(child.artifact.toString, pom.artifact.toString)))

    // determine external parent projects
    pomNodes.foreach(pom => {
      pom.parent.foreach(parent => {
        determineExternalParents(pom, parent)
      })
    })

    determineGraphRoutes
    updateArtifactCoordinates()
    refreshNodeKeys()
    markParentEdges(pomNodes)

    pomNodes
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
        parentPom = Pom(pomFile)
        parentPom.childPoms.addOne(pom)
        nodeCache.addOne(parentArtifact.key, parentPom)
      }
    }
    if (parentPom != null) {
      val name = parentPom.artifact.toString
      addNode(PomNode(this, name, parentPom, isExternal = true))
      addEdgeById(pom.artifact.toString, name)
      parentPom.parent.foreach(parent => {
        determineExternalParents(parentPom, parent)
      })
    }
  }

  private def updateArtifactCoordinates(): Unit = {
    dfsSet.toSeq.reverse.foreach(dotNode => {
      val pomNode = dotNode.asInstanceOf[PomNode]
      val pom = pomNode.pom
      pom.parent.foreach(pom.artifact.update(_, UpdateMode.none))
      setMavenProperties(pom)

      pom.dependencyManagement.values.foreach(dm => {
        dm.resolveProperties()
        pom.dependencies.get(dm.key).foreach(dep => {
          dep.update(dm, UpdateMode.management)
        })
      })
      refreshKeys(pom.dependencyManagement)

      pom.dependencies.values.foreach(d => {
        d.resolveProperties()
      })
      refreshKeys(pom.dependencies)

      pom.build.foreach(build => {
        build.pluginManagement.values.foreach(pm => {
          pm.resolveProperties()
          build.plugins.get(pm.key).foreach(p => {
            p.update(pm, UpdateMode.management)
          })
        })
        refreshKeys(build.pluginManagement)
        build.plugins.values.foreach(p => {
          p.resolveProperties()
        })
        refreshKeys(build.plugins)
      })

      pomNode.pom.childPoms.foreach(child => {
        child.artifact.update(pom.artifact, UpdateMode.none)

        // derive properties
        // only override values which either do not exist in the poms properties or are empty
        pom.properties.foreach(elem => {
          val value = child.properties.get(elem._1)
          if (value.isEmpty || value.get.isEmpty) {
            val p = elem._2.clone()
            p.derived = true
            child.properties.addOne(elem._1, p)
          }
        })

        // process dependency management node (if any)
        child.dependencyManagement.values.foreach(_.resolveProperties())
        refreshKeys(child.dependencyManagement)

        // process dependencies node (if any)
        child.dependencies.values.foreach(d => d.resolveProperties())
        refreshKeys(child.dependencies)

        // derive dependency management
        pom.dependencyManagement.foreach(elem => {
          val dep = child.dependencyManagement.get(elem._2.key)
          if (dep.nonEmpty) dep.get.update(elem._2, UpdateMode.resolved)
          else {
            val dep = elem._2.clone()
            dep.derived = true
            child.dependencyManagement.addOne(elem._1, dep)
          }
        })

        // resolve dependencies from management
        child.dependencyManagement.values.foreach(d => {
          child.dependencies.get(d.key).foreach(dep => {
            dep.update(d, UpdateMode.management)
          })
        })

        // process build node (if any)
        pom.build.foreach(pb => {
          child.build.foreach(cb => {
            // process plugin management node (if any)
            cb.pluginManagement.values.foreach(_.resolveProperties())
            refreshKeys(cb.pluginManagement)

            // process plugins node (if any)
            cb.plugins.values.foreach(_.resolveProperties())
            refreshKeys(cb.plugins)

            // derive plugin management
            pb.pluginManagement.foreach(pe => {
              val p = cb.pluginManagement.get(pe._2.key)
              if (p.nonEmpty) p.get.update(pe._2, UpdateMode.resolved)
              else {
                val p = pe._2.clone()
                p.derived = true
                cb.pluginManagement.addOne(pe._1, p)
              }
            })

            // resolve plugins from management
            cb.pluginManagement.values.foreach(d => {
              cb.plugins.get(d.key).foreach(p => {
                p.update(d, UpdateMode.management)
              })
            })
          })
        })
      })
    })

    // finally generate the dot tables
    dfsSet.foreach(_.asInstanceOf[PomNode].showAttributes())
  }

  private def setMavenProperties(pom: Pom): Unit = {
    val baseDir = pom.pomFile.getParentFile.getCanonicalFile
    pom.properties.addOne("basedir", PropertyValue(baseDir.getPath))
    pom.properties.addOne("project.basedir", PropertyValue(baseDir.getPath))
    pom.properties.addOne("project.baseUri", PropertyValue(baseDir.toURI.toString))
    pom.properties.addOne("maven.multiModuleProjectDirectory", PropertyValue(baseDir.getPath))

    pom.properties.addOne("project.packaging", PropertyValue(pom.packaging))
    pom.properties.addOne("project.groupId", PropertyValue(pom.artifact.groupId))
    pom.properties.addOne("project.artifactId", PropertyValue(pom.artifact.artifactId))
    pom.properties.addOne("project.name", PropertyValue(pom.artifact.name))
    pom.properties.addOne("project.description", PropertyValue(pom.artifact.description))
    pom.properties.addOne("project.version", PropertyValue(pom.artifact.version))

    var buildDirectory = if (pom.build.nonEmpty && pom.build.get.directory.nonEmpty) Paths.get(baseDir.getPath, pom.build.get.directory).toFile.getCanonicalPath else ""
    buildDirectory = if (buildDirectory.isEmpty) Paths.get(baseDir.getPath, "target").toFile.getCanonicalPath else buildDirectory
    pom.properties.addOne("project.build.directory", PropertyValue(buildDirectory))

    var sourceDirectory = if (pom.build.nonEmpty && pom.build.get.sourceDirectory.nonEmpty) Paths.get(baseDir.getPath, pom.build.get.sourceDirectory).toFile.getCanonicalPath else ""
    sourceDirectory = if (sourceDirectory.isEmpty) Paths.get(baseDir.getPath, "src", "main", "java").toFile.getCanonicalPath else sourceDirectory
    pom.properties.addOne("project.build.sourceDirectory", PropertyValue(sourceDirectory))

    var outputDirectory = if (pom.build.nonEmpty && pom.build.get.outputDirectory.nonEmpty) Paths.get(baseDir.getPath, pom.build.get.outputDirectory).toFile.getCanonicalPath else ""
    outputDirectory = if (outputDirectory.isEmpty) Paths.get(buildDirectory, "classes").toFile.getCanonicalPath else outputDirectory
    pom.properties.addOne("project.build.outputDirectory", PropertyValue(outputDirectory))

    var testSourceDirectory = if (pom.build.nonEmpty && pom.build.get.testSourceDirectory.nonEmpty) Paths.get(baseDir.getPath, pom.build.get.testSourceDirectory).toFile.getCanonicalPath else ""
    testSourceDirectory = if (testSourceDirectory.isEmpty) Paths.get(baseDir.getPath, "src", "test", "java").toFile.getCanonicalPath else testSourceDirectory
    pom.properties.addOne("project.build.testSourceDirectory", PropertyValue(testSourceDirectory))

    var testOutputDirectory = if (pom.build.nonEmpty && pom.build.get.testOutputDirectory.nonEmpty) Paths.get(baseDir.getPath, pom.build.get.testOutputDirectory).toFile.getCanonicalPath else ""
    testOutputDirectory = if (testOutputDirectory.isEmpty) Paths.get(buildDirectory, "test-classes").toFile.getCanonicalPath else testOutputDirectory
    pom.properties.addOne("project.build.testOutputDirectory", PropertyValue(testOutputDirectory))
  }

  private def refreshNodeKeys(): Unit = {
    val nodesTemp: mutable.Map[String, DotNode] = mutable.TreeMap()
    nodes.values.foreach(node => {
      nodesTemp.addOne(node.asInstanceOf[PomNode].pom.artifact.toString, node)
    })
    nodes.clear()
    nodes.addAll(nodesTemp)
  }

  /**
   * Recalculates the keys of the given dependencies by re assigning the values to their current keys.
   *
   * @param dependencies The dependencies.
   * @tparam T
   */
  def refreshKeys[T <: Artifact](dependencies: mutable.Map[String, T]): Unit = {
    val artifacts: mutable.Map[String, T] = mutable.Map()
    dependencies.values.foreach(dm => {
      artifacts.addOne(dm.key, dm)
    })
    dependencies.clear()
    dependencies.addAll(artifacts.asInstanceOf[mutable.Map[String, T]])
    artifacts.clear()
  }
}

object PomGraph {
  def apply(pomFile: File, name: String = "") = new PomGraph(pomFile, name)
}