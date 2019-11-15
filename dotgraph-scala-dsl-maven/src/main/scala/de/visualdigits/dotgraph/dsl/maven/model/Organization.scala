package de.visualdigits.dotgraph.dsl.maven.model

import scala.xml.Node

class Organization(node: Node) extends MavenObject {

  val name: String = (node \ "name").map(_.text).headOption.getOrElse("")
  val url: String = (node \ "url").map(_.text).headOption.getOrElse("")

  override def toString: String = {
    "%s: %s".format(name, url)
  }
}

object Organization {
  def apply(node: Node) = new Organization(node)
}
