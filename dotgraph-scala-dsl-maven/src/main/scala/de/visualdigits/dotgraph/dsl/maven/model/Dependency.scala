package de.visualdigits.dotgraph.dsl.maven.model

import scala.xml.Node

class Dependency(node: Node, pom: Pom) extends Artifact(node, pom) {

  var scope: String = (node \ "scope").map(_.text).headOption.getOrElse("compile")

  def this(node: Node, pom: Pom, artifact: Artifact = null) {
    this(node, pom)
    if (artifact != null) {
      groupId = artifact.groupId
      artifactId = artifact.artifactId
      version = artifact.version
      name = artifact.name
      description = artifact.description
      updateMode = artifact.updateMode
      derived = artifact.derived
    }
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