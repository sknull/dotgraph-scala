package de.visualdigits.dotgraph.dsl.maven.model

import de.visualdigits.dotgraph.dsl.maven.model.`type`.UpdateMode

import scala.xml.Node

class Artifact(node: Node, val pom: Pom) extends MavenObject with Ordering[Artifact] {

  var groupId: String = (node \ "groupId").map(_.text).headOption.getOrElse("")
  var artifactId: String = (node \ "artifactId").map(_.text).headOption.getOrElse("")
  var version: String = (node \ "version").map(_.text).headOption.getOrElse("")
  var name: String = (node \ "name").map(_.text).headOption.getOrElse("")
  var description: String = (node \ "description").map(_.text).headOption.getOrElse("")

  var updateMode: UpdateMode = UpdateMode.none

  var derived: Boolean = false

  override def toString: String = {
    "%s:%s:%s".format(groupId, artifactId, version)
  }

  override def clone(): Artifact = {
    val clone: Artifact = Artifact(node, pom)
    clone.groupId = groupId
    clone.artifactId = artifactId
    clone.version = version
    clone.name = name
    clone.description = description
    clone.updateMode = updateMode
    clone.derived = derived
    clone
  }

  def key: String = {
    "%s:%s".format(groupId, artifactId)
  }

  def update(other: Artifact, mode: UpdateMode): Unit = {
    if (groupId.isEmpty && other.groupId.nonEmpty) {
      groupId = other.groupId
      this.updateMode = mode
    }

    if (artifactId.isEmpty && other.artifactId.nonEmpty) {
      artifactId = other.artifactId
      this.updateMode = mode
    }

    if (version.isEmpty && other.version.nonEmpty) {
      this.version = other.version
      this.updateMode = mode
    }

    if (name.isEmpty && other.name.nonEmpty) {
      name = other.name
      this.updateMode = mode
    }

    if (description.isEmpty && other.description.nonEmpty) {
      description = other.description
      this.updateMode = mode
    }
  }

  def resolveProperties(): Artifact = {
    val props = pom.properties.toMap
    groupId = subst(groupId, props)
    artifactId = subst(artifactId, props)
    version = subst(version, props)
    name = subst(name, props)
    description = subst(description, props)
    this
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Artifact]

  override def equals(other: Any): Boolean = other match {
    case that: Artifact =>
      (that canEqual this) &&
        groupId == that.groupId &&
        artifactId == that.artifactId
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(groupId, artifactId)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def compare(x: Artifact, y: Artifact): Int = {
    x.hashCode().compare(y.hashCode())
  }
}

object Artifact {
  def apply(node: Node, pom: Pom) = new Artifact(node, pom)
}