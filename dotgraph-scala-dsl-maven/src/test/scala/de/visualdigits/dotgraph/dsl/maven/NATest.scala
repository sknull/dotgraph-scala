package de.visualdigits.dotgraph.dsl.maven

import java.io.File
import java.nio.file.Paths

import de.visualdigits.dotgraph.dsl.maven.dotgraph.PomGraph
import org.junit.Test

class NATest {

  private val userHome: String = System.getProperty("user.home")
  private val m2RepoDirectory: String = Paths.get(userHome, ".m2", "repository").toString
  private val targetDirectory: File = Paths.get(userHome, "dotgraph", "MediaBoss").toFile

  @Test
  def testXml(): Unit = {
    val rootPackage = "de.newsaktuell.mb"
    val graph = PomGraph(new File("/Users/knull/git/mb-business/pom.xml"),
      m2RepoDirectory = m2RepoDirectory,
      rootPackages = Seq(rootPackage)
    )
    graph.nodes.values
      .filter(!_.id.startsWith(rootPackage))
      .foreach(n => graph.removeNodeById(n.id))

    graph.nodes.values
      .filter(_.id.startsWith(rootPackage))
      .foreach(n =>
        n.edges
          .filter(!_.to.id.startsWith(rootPackage))
          .foreach(e => n.edges.remove(e))
      )

    graph.determineGraphRoutes
      .output(targetDirectory)
  }
}
