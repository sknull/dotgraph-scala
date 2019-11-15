package de.visualdigits.dotgraph.dsl.maven

import java.io.File

import de.visualdigits.dotgraph.core.util.system.OsUtil
import de.visualdigits.dotgraph.dsl.maven.dotgraph.PomGraph
import org.junit.Test

class XmlTest {

  val targetDirectory: File = new File(System.getProperty("user.home"), "dotgraph")

  @Test def testXml(): Unit = {
    PomGraph(new File("../pom.xml")).output(targetDirectory, format="jpg", dpi = 300)
  }
}
