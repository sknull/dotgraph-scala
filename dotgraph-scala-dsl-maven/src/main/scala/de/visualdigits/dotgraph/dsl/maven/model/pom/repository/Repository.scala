package de.visualdigits.dotgraph.dsl.maven.model.pom.repository

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.visualdigits.dotgraph.dsl.maven.model.common.MavenObject
import de.visualdigits.dotgraph.dsl.maven.model.pom.Pom

import scala.xml.Node

@JsonIgnoreProperties(Array("node"))
class Repository(node: Node, pom: Pom) extends MavenObject(node, pom) {

  val id: String = (node \ "id").map(_.text).headOption.getOrElse("")
  val name: String = (node \ "name").map(_.text).headOption.getOrElse("")
  val url: String = (node \ "url").map(_.text).headOption.getOrElse("")

  override def toString: String = {
    "Releases: [%s] %s: %s".format(id, name, url)
  }
}

object Repository {
  def apply(node: Node, pom: Pom) = new Repository(node, pom)
}
