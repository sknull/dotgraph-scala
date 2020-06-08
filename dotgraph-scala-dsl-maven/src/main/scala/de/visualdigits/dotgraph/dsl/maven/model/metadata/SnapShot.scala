package de.visualdigits.dotgraph.dsl.maven.model.metadata

import de.visualdigits.dotgraph.core.xmlmodel.XmlNode

import scala.xml.Node

class SnapShot(node: Node, metaData: MetaData) extends XmlNode[MetaData](node, metaData){
  val localCopy: Boolean = (node \ "localCopy").map(_.text).headOption.getOrElse("false").toBoolean
  val timestamp: String = (node \ "timestamp").map(_.text).headOption.getOrElse("")
  val buildNumber: String = (node \ "buildNumber").map(_.text).headOption.getOrElse("")
}

object SnapShot {
  def apply(node: Node, metaData: MetaData) = new SnapShot(node, metaData)
}