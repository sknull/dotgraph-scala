package de.visualdigits.dotgraph.dsl.maven.model.pom

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.visualdigits.dotgraph.dsl.maven.model.common.MavenObject

import scala.xml.Node

@JsonIgnoreProperties(Array("node", "pom"))
class Resource(node: Node, pom: Pom) extends MavenObject(node, pom) {

  var directory: String = (node \ "directory").map(_.text).headOption.getOrElse("")
  var includes: List[String] = (node \ "includes" \ "include").map(_.text).toList
  var excludes: List[String] = (node \ "excludes" \ "exclude").map(_.text).toList
}

object Resource {
  def apply(node: Node, pom: Pom) = new Resource(node, pom)
}
