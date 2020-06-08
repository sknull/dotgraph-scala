package de.visualdigits.dotgraph.dsl.maven.model.pom.artifact

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.visualdigits.dotgraph.dsl.maven.model.pom.Pom

import scala.xml.Node

@JsonIgnoreProperties(Array("node", "pom"))
class Dependency(node: Node, pom: Pom) extends Artifact(node, pom) {

  var scope: String = (node \ "scope").map(_.text).headOption.getOrElse("compile")

  def this(node: Node, pom: Pom, artifact: Artifact = null) {
    this(node, pom)
    determineCoordinates(artifact)
  }

  val exclusions: Set[Artifact] = {
    (node \\ "exclusion").map(Artifact(_, pom)).toSet
  }

  override def toString: String = {
    super.toString() + ":%s".format(scope)
  }

  override def clone(): Dependency = {
    val clone = Dependency(node, pom, super.clone())
    clone.scope = scope
    clone
  }
}

object Dependency {
  def apply(node: Node, pom: Pom, artifact: Artifact = null) = new Dependency(node, pom, artifact)
}