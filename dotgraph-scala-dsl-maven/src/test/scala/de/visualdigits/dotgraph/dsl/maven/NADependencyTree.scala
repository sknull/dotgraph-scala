package de.visualdigits.dotgraph.dsl.maven

import java.io.{File, FileFilter}
import java.nio.file.Paths

import de.visualdigits.dotgraph.core.`type`.CreateLegend
import de.visualdigits.dotgraph.core.entity.{DotColor, DotGraph, DotNode}
import de.visualdigits.dotgraph.dsl.maven.dotgraph.PomNode
import de.visualdigits.dotgraph.dsl.maven.model.pom.Pom
import org.junit.Test

class NADependencyTree {

  private val userHome: String = System.getProperty("user.home")
  private val m2RepoDirectory: String = Paths.get(userHome, ".m2", "repository").toString
  private val targetDirectory: File = Paths.get(userHome, "dotgraph", "MediaBoss").toFile

  @Test
  def test: Unit = {
    val graph = DotGraph("NA-Dependencies")
      .setCreateLegend(CreateLegend.FULL)
      .addCategory("Root Nodes", Some(DotColor.RED_LIGHT))
      .addCategory("Normal Nodes", Some(DotColor.YELLOW_LIGHT))
      .addCategory("Leaf Nodes", Some(DotColor.GREEN_LIGHT))

    val includePackages = Seq("de.newsaktuell.mb")
    recurse(graph, new File("/Users/knull/git"), includePackages = includePackages, ignoreDirectories = Seq("target", "src", "docs", "resources"))
    graph.nodes.foreach(entry => {
      val artifact = entry._2.asInstanceOf[PomNode].pom
      val artifactId = s"${artifact.artifactGroupId()}:${artifact.artifactId()}"
      artifact.dependencies
        .filter(dependency => includePackages.contains(dependency._2.groupId))
        .foreach(dependency => {
          val dependencyId = s"${dependency._2.groupId}:${dependency._2.artifactId}"
          if (graph.containsNodeById(dependencyId)) graph.addEdgeById(artifactId, dependencyId)
        })
    })
    graph.determineGraphRoutes
    graph.nodes.values
      .filter(node => node.edges.isEmpty && node.references.isEmpty)
      .map(_.id)
      .toSeq
      .foreach(orphan => graph.removeNodeById(orphan))
    graph.nodes.foreach(node => node._2.asInstanceOf[PomNode].table.rows.head.cells.head.attributes.setBgcolor(DotColor.YELLOW_LIGHT))
    graph.roots.foreach(root => root.asInstanceOf[PomNode].table.rows.head.cells.head.attributes.setBgcolor(DotColor.RED_LIGHT))
    graph.leafs.foreach(leaf => leaf.asInstanceOf[PomNode].table.rows.head.cells.head.attributes.setBgcolor(DotColor.GREEN_LIGHT))
    graph.output(targetDirectory)
  }

  def recurse(graph: DotGraph, rootDirectory: File, level: Int = 0, includePackages: Seq[String] = Seq(), ignoreDirectories: Seq[String] = Seq()): Unit = {
    val pomFiles = rootDirectory.listFiles(new FileFilter {
      override def accept(file: File): Boolean = {
        file.isFile && file.getName == "pom.xml"
      }
    })
    pomFiles
      .foreach(pf => {
      val pom = Pom(pf, m2RepoDirectory)
      if (includePackages.contains(pom.artifactGroupId())) {
        val id = s"${pom.artifactGroupId()}:${pom.artifactId()}"
        graph.addNode(PomNode(graph, id, pom, m2RepoDirectory))
      }
    })
    val dirs = rootDirectory.listFiles(new FileFilter {
      override def accept(file: File): Boolean = {
        file.isDirectory && !ignoreDirectories.exists(dir => file.getAbsolutePath.contains(dir))
      }
    })
    dirs
      .foreach(directory =>
      recurse(graph, directory, level + 1, includePackages, ignoreDirectories))
  }
}
