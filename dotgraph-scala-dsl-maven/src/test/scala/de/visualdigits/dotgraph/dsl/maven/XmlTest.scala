package de.visualdigits.dotgraph.dsl.maven

import java.io.File
import java.nio.file.Paths

import de.visualdigits.dotgraph.dsl.maven.dotgraph.PomGraph
import org.junit.Test

class XmlTest {

  private val userHome: String = System.getProperty("user.home")
  private val m2RepoDirectory: String = Paths.get(userHome, ".m2", "repository").toString
  private val targetDirectory: File = Paths.get(userHome, "dotgraph", "maven").toFile

  @Test
  def testXml(): Unit = {
    PomGraph(new File("../pom.xml"),
      m2RepoDirectory = m2RepoDirectory,
      rootPackages = Seq("de.visualdigits"),
      analyzeDependencies = true
    ).output(targetDirectory)
  }
}
