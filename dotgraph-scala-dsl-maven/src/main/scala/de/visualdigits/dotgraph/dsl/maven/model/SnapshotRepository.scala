package de.visualdigits.dotgraph.dsl.maven.model

import scala.xml.Node

class SnapshotRepository(node: Node) extends Repository(node) {

  override def toString: String = {
    "Snapshots: [%s] %s: %s".format(id, name, url)
  }
}

object SnapshotRepository {
  def apply(node: Node) = new SnapshotRepository(node)
}
