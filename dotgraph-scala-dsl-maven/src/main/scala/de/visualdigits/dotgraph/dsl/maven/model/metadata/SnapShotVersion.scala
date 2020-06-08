package de.visualdigits.dotgraph.dsl.maven.model.metadata

import de.visualdigits.dotgraph.core.xmlmodel.XmlNode

import scala.xml.Node

class SnapShotVersion(node: Node, metaData: MetaData) extends XmlNode[MetaData](node, metaData) with Ordering[SnapShotVersion] {
  val extension: String = (node \ "extension").map(_.text).headOption.getOrElse("")
  val value: String = (node \ "value").map(_.text).headOption.getOrElse("")
  val updated: String = (node \ "updated").map(_.text).headOption.getOrElse("")

  def canEqual(other: Any): Boolean = other.isInstanceOf[SnapShotVersion]

  override def equals(other: Any): Boolean = other match {
    case that: SnapShotVersion =>
      (that canEqual this) &&
        extension == that.extension &&
        value == that.value
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(extension, value)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  private def compareCode(): Int = {
    val state = Seq(extension, value, updated)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def compare(x: SnapShotVersion, y: SnapShotVersion): Int = {
    x.compareCode().compare(y.compareCode())
  }
}

object SnapShotVersion {
  def apply(node: Node, metaData: MetaData) = new SnapShotVersion(node, metaData)
}