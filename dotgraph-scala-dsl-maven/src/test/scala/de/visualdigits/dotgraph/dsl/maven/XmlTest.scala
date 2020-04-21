package de.visualdigits.dotgraph.dsl.maven

import java.io.File

import de.visualdigits.dotgraph.dsl.maven.dotgraph.PomGraph
import de.visualdigits.dotgraph.dsl.maven.model.Pom
import org.junit.Test

class XmlTest {

  val targetDirectory: File = new File(System.getProperty("user.home"), "dotgraph")

  @Test
  def testXml(): Unit = {
    PomGraph(new File("../pom.xml")).output(targetDirectory, format="jpg", dpi = 300)
  }

  @Test
  def testPom(): Unit = {
    val pom = Pom(new File("../pom.xml"))
    println(pom)
  }
}
