package de.visualdigits.dotgraph.dsl.maven.model.pom

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.visualdigits.dotgraph.dsl.maven.model.common.MavenObject

import scala.xml.Node

@JsonIgnoreProperties(Array("node"))
class Scm(node: Node, pom: Pom) extends MavenObject(node, pom) {

  val connection: String = (node \ "connection").map(_.text).headOption.getOrElse("")
  val developerConnection: String = (node \ "developerConnection").map(_.text).headOption.getOrElse("")
  val url: String = (node \ "url").map(_.text).headOption.getOrElse("")
}

object Scm {
  def apply(node: Node, pom: Pom) = new Scm(node, pom)
}
