package de.visualdigits.dotgraph.dsl.maven.model

import scala.xml.Node

class Resource(node: Node, pom: Pom) extends MavenObject {

  var directory: String = (node \ "directory").map(_.text).headOption.getOrElse("")
  var includes: List[String] = (node \ "includes" \ "include").map(_.text).toList
  var excludes: List[String] = (node \ "excludes" \ "exclude").map(_.text).toList
}

object Resource {
  def apply(node: Node, pom: Pom) = new Resource(node, pom)
}
