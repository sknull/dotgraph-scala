package de.visualdigits.dotgraph.dsl.maven.model.pom

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.visualdigits.dotgraph.dsl.maven.model.common.MavenObject

import scala.xml.Node

@JsonIgnoreProperties(Array("node", "pom"))
class Configuration(node: Node, pom: Pom) extends MavenObject(node, pom) {

  val includes: List[String] = (node \ "includes" \ "include").map(_.text).toList
  val excludes: List[String] = (node \ "excludes" \ "exclude").map(_.text).toList
}

object Configuration {
  def apply(node: Node, pom: Pom) = new Configuration(node, pom)
}
