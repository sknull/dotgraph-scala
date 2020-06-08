package de.visualdigits.dotgraph.dsl.maven.model.pom

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.visualdigits.dotgraph.dsl.maven.model.common.MavenObject

import scala.xml.Node

@JsonIgnoreProperties(Array("node", "pom"))
class Profile(node: Node, pom: Pom) extends MavenObject(node, pom) {

  var id: String = (node \ "id").map(_.text).headOption.getOrElse("")
  var build: Option[Build] = (node \ "build").map(Build(_, pom)).headOption
}

object Profile {
  def apply(node: Node, pom: Pom) = new Profile(node, pom)
}
