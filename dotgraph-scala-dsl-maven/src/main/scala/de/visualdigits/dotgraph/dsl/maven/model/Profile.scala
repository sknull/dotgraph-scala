package de.visualdigits.dotgraph.dsl.maven.model

import scala.xml.Node

class Profile(node: Node, pom: Pom) extends MavenObject {

  var id: String = (node \ "id").map(_.text).headOption.getOrElse("")
  var build: Option[Build] = (node \ "build").map(Build(_, pom)).headOption
}

object Profile {
  def apply(node: Node, pom: Pom) = new Profile(node, pom)
}
