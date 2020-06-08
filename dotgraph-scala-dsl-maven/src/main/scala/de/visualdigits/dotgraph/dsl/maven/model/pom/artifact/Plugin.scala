package de.visualdigits.dotgraph.dsl.maven.model.pom.artifact

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.visualdigits.dotgraph.dsl.maven.model
import de.visualdigits.dotgraph.dsl.maven.model.pom.{Configuration, Execution, Pom}

import scala.xml.Node

@JsonIgnoreProperties(Array("node", "pom"))
class Plugin(node: Node, pom: Pom) extends Artifact(node, pom) {

  var configuration: Option[Configuration] = (node \ "configuration").map(model.pom.Configuration(_, pom)).headOption
  var executions: Seq[Execution] = (node \\ "execution").map(Execution(_, pom))

  def this(node: Node, pom: Pom, artifact: Artifact = null) {
    this(node, pom)
    determineCoordinates(artifact)
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
