package de.visualdigits.dotgraph.dsl.maven.model

import scala.xml.Node

class Repository(node: Node) extends MavenObject {

  val id: String = (node \ "id").map(_.text).headOption.getOrElse("")
  val name: String = (node \ "name").map(_.text).headOption.getOrElse("")
  val url: String = (node \ "url").map(_.text).headOption.getOrElse("")

  override def toString: String = {
    "Releases: [%s] %s: %s".format(id, name, url)
  }
}

object Repository {
  def apply(node: Node) = new Repository(node)
}
