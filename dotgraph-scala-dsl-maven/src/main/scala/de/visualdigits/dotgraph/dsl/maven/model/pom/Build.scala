package de.visualdigits.dotgraph.dsl.maven.model.pom

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.visualdigits.dotgraph.dsl.maven.model.common.MavenObject
import de.visualdigits.dotgraph.dsl.maven.model.pom.artifact.Plugin

import scala.collection.mutable
import scala.xml.Node

@JsonIgnoreProperties(Array("node", "pom"))
class Build(node: Node, pom: Pom) extends MavenObject(node, pom) {

  var finalName: String = (node \ "finalName").map(_.text).headOption.getOrElse("")
  var directory: String = (node \ "directory").map(_.text).headOption.getOrElse("")
  var outputDirectory: String = (node \ "outputDirectory").map(_.text).headOption.getOrElse("")
  var sourceDirectory: String = (node \ "sourceDirectory").map(_.text).headOption.getOrElse("")
  var testOutputDirectory: String = (node \ "testOutputDirectory").map(_.text).headOption.getOrElse("")
  var testSourceDirectory: String = (node \ "testSourceDirectory").map(_.text).headOption.getOrElse("")
  val pluginManagement: mutable.Map[String, Plugin] = {
    mutable.Map().addAll((node \ "pluginManagement" \ "plugins" \ "plugin").map(p => {
      val plug = artifact.Plugin(p, pom)
      (plug.key, plug)
    }))
  }
  val plugins: mutable.Map[String, Plugin] = {
    mutable.Map().addAll((node \ "plugins" \ "plugin").map(p => {
      val plug = artifact.Plugin(p, pom)
      (plug.key, plug)
    }))
  }
  val resources: List[Resource] = (node \ "resources" \ "resource").map(Resource(_, pom)).toList
  val testResources: List[Resource] = (node \ "testResources" \ "testResource").map(Resource(_, pom)).toList

  def resolveProperties(): Build = {
    val props = pom.properties.toMap
    finalName = subst(finalName, props)
    sourceDirectory = subst(sourceDirectory, props)
    testSourceDirectory = subst(testSourceDirectory, props)
    this
  }
}

object Build {
  def apply(node: Node, pom: Pom) = new Build(node, pom)
}
