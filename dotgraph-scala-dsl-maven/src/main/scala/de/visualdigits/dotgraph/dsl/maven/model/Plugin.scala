package de.visualdigits.dotgraph.dsl.maven.model

import scala.xml.Node

class Plugin(node: Node, pom: Pom) extends Artifact(node, pom) {

  var configuration: Option[Node] = (node \ "configuration").headOption
  var executions: Seq[Execution] = (node \\ "execution").map(Execution(_))

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

  override def toString: String = {
    super.toString() + ": %s".format(executions.mkString(", "))
  }

  override def clone(): Plugin = {
    val clone = Plugin(node, pom, super.clone())
    clone.configuration = configuration
    clone.executions = executions
    clone
  }
}

object Plugin {
  def apply(node: Node, pom: Pom, artifact: Artifact = null) = new Plugin(node, pom, artifact)
}
