package de.visualdigits.dotgraph.dsl.maven.model.common

import de.visualdigits.dotgraph.core.xmlmodel.XmlNode
import de.visualdigits.dotgraph.dsl.maven.model.pom.Pom

import scala.xml.Node

class MavenObject(node: Node, rootNode: Pom) extends XmlNode[Pom](node, rootNode) {

  def subst(s: String, m: Map[String, PropertyValue]): String = {
    m.foldLeft(s){ case (newState, kv) => newState.replace('$' + s"{${kv._1}}", kv._2.value)}
  }
}

object MavenObject {
  def apply(node: Node, rootNode: Pom) = new MavenObject(node, rootNode)
}