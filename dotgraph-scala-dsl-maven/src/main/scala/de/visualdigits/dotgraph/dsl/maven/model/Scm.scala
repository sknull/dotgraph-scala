package de.visualdigits.dotgraph.dsl.maven.model

import scala.xml.Node

class Scm(node: Node) extends MavenObject {

  val connection: String = (node \ "connection").map(_.text).headOption.getOrElse("")
  val developerConnection: String = (node \ "developerConnection").map(_.text).headOption.getOrElse("")
  val url: String = (node \ "url").map(_.text).headOption.getOrElse("")
}

object Scm {
  def apply(node: Node) = new Scm(node)
}
