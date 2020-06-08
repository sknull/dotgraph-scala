package de.visualdigits.dotgraph.dsl.maven.model.pom

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.visualdigits.dotgraph.dsl.maven.model.common.MavenObject

import scala.xml.Node

@JsonIgnoreProperties(Array("node"))
class Execution(node: Node, pom: Pom) extends MavenObject(node, pom) {

  val phase: String =(node \ "phase").map(_.text).headOption.getOrElse("")
  val goals: Set[String] = (node \\ "goal").map(_.text).toSet

  override def toString: String = {
    "[%s: %s]".format(phase, goals.mkString(", "))
  }
}

object Execution {
  def apply(node: Node, pom: Pom) = new Execution(node, pom)
}
