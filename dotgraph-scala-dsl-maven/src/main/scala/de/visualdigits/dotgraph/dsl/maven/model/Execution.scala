package de.visualdigits.dotgraph.dsl.maven.model

import scala.xml.Node

class Execution(node: Node) extends MavenObject {

  val phase: String =(node \ "phase").map(_.text).headOption.getOrElse("")
  val goals: Set[String] = (node \\ "goal").map(_.text).toSet

  override def toString: String = {
    "[%s: %s]".format(phase, goals.mkString(", "))
  }
}

object Execution {
  def apply(node: Node) = new Execution(node)
}
