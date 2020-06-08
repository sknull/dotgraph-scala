package de.visualdigits.dotgraph.dsl.maven.model.pom.repository

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.visualdigits.dotgraph.dsl.maven.model.pom.Pom

import scala.xml.Node

@JsonIgnoreProperties(Array("node"))
class SnapshotRepository(node: Node, pom: Pom) extends Repository(node, pom) {

  override def toString: String = {
    "Snapshots: [%s] %s: %s".format(id, name, url)
  }
}

object SnapshotRepository {
  def apply(node: Node, pom: Pom) = new SnapshotRepository(node, pom)
}
