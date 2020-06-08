package de.visualdigits.dotgraph.dsl.maven.model.metadata

import de.visualdigits.dotgraph.core.xmlmodel.XmlNode

import scala.xml.Node

class Versioning(node: Node, metaData: MetaData) extends XmlNode[MetaData](node, metaData){
  val snapShot: SnapShot = (node \ "snapshot").map(SnapShot(_, metaData)).headOption.orNull
  val lastUpdated: String = (node \ "lastUpdated").map(_.text).headOption.getOrElse("")
  val snapShotVersions: Seq[SnapShotVersion] = (node \ "snapshotVersions" \ "snapshotVersion").map(SnapShotVersion(_, metaData))
}

object Versioning {
  def apply(node: Node, metaData: MetaData) = new Versioning(node, metaData)
}