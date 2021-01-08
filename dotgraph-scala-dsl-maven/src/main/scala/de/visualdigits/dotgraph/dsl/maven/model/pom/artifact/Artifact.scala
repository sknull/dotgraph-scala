package de.visualdigits.dotgraph.dsl.maven.model.pom.artifact

import java.nio.file.Paths

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.visualdigits.dotgraph.dsl.maven.model.common.{MavenObject, PropertyValue}
import de.visualdigits.dotgraph.dsl.maven.model.metadata.MetaData
import de.visualdigits.dotgraph.dsl.maven.model.pom.Pom
import de.visualdigits.dotgraph.dsl.maven.model.pom.`type`.UpdateMode

import scala.xml.Node

@JsonIgnoreProperties(Array("node", "pom"))
class Artifact(node: Node, val pom: Pom) extends MavenObject(node, pom) with Ordered[Artifact] {

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

  protected def determineCoordinates(artifact: Artifact): Unit = {
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

  /**
   * Returns whether this artifact has at least groupId artifactId and version set.
   *
   * @return Boolean
   */
  def isComplete(): Boolean = {
    return groupId.nonEmpty && artifactId.nonEmpty && version.nonEmpty
  }

  /**
   * Returns the relative path to this artifact directory in a maven repository.
   *
   * @return String
   */
  def artifactPath(): String = {
    Paths.get(groupId.replace('.', '/'), artifactId, version).toString
  }

  def loadPomFromLocalRepo(m2RepoDirectory: String): Option[Pom] = {
    val path = artifactPath()
    val localMetaDataFile = Paths.get(m2RepoDirectory, path, "maven-metadata-local.xml").toFile
    val pomFileName = if (localMetaDataFile.exists()) {
      val metaData = MetaData(localMetaDataFile)
      metaData.versioning.snapShotVersions.headOption.map(v => s"${metaData.artifactId}-${v.value}.pom").getOrElse("")
    } else {
      s"$artifactId-$version.pom"
    }
    val pomFile = Paths.get(m2RepoDirectory, path, pomFileName).toFile
    if (pomFile.exists() && pomFile.isFile) {
      Some(Pom(pomFile, m2RepoDirectory, None))
    } else {
      None
    }
  }

  def key: String = {
    "%s:%s".format(groupId, artifactId)
  }

  def update(other: Artifact, mode: UpdateMode = UpdateMode.none): Unit = {
    this.updateMode = mode

    if (groupId.isEmpty && other.groupId.nonEmpty) {
      groupId = other.groupId
    }

    if (artifactId.isEmpty && other.artifactId.nonEmpty) {
      artifactId = other.artifactId
    }

    if (version.isEmpty && other.version.nonEmpty) {
      this.version = other.version
    }

    if (name.isEmpty && other.name.nonEmpty) {
      name = other.name
    }

    if (description.isEmpty && other.description.nonEmpty) {
      description = other.description
    }
  }

  def resolveProperties(props: Map[String, PropertyValue]): Artifact = {
    groupId = subst(groupId, props)
    artifactId = subst(artifactId, props)
    version = subst(artifactVersion(props), props)
    name = subst(name, props)
    description = subst(description, props)
    this
  }

  private def artifactVersion(props: Map[String, PropertyValue]): String = {
    if (version.nonEmpty) version else props.get("project.version").map(_.value).getOrElse("")
  }

  override def compare(that: Artifact): Int = {
    val a = s"$groupId$artifactId$version"
    val b = s"${that.groupId}${that.artifactId}${that.version}"
    a.compare(b)
  }
}

object Artifact {
  def apply(node: Node, pom: Pom) = new Artifact(node, pom)
}
