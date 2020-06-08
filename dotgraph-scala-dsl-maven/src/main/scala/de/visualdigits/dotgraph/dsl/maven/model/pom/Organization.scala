package de.visualdigits.dotgraph.dsl.maven.model.pom

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.visualdigits.dotgraph.dsl.maven.model.common.MavenObject

import scala.xml.Node

@JsonIgnoreProperties(Array("node"))
class Organization(node: Node, pom: Pom) extends MavenObject(node, pom) {

  val name: String = (node \ "name").map(_.text).headOption.getOrElse("")
  val url: String = (node \ "url").map(_.text).headOption.getOrElse("")

  override def toString: String = {
    "%s: %s".format(name, url)
  }
}

object Organization {
  def apply(node: Node, pom: Pom) = new Organization(node, pom)
}
