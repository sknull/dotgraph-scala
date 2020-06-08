package de.visualdigits.dotgraph.dsl.maven.model.metadata

import java.io.{File, InputStream}

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.visualdigits.dotgraph.core.xmlmodel.XmlNode
import de.visualdigits.dotgraph.dsl.maven.model.pom.artifact.Artifact

import scala.xml.{Node, XML}


@JsonIgnoreProperties(Array("root", "parentPom", "childPoms", "pomFile"))
class MetaData(val root: Node, val metaDataFile: Option[File]) extends XmlNode[MetaData](root, null) with Ordered[MetaData] {

  val modelVersion: String = (root \ "@modelVersion").map(_.text).headOption.getOrElse("")
  val groupId: String = (node \ "groupId").map(_.text).headOption.getOrElse("")
  val artifactId: String = (node \ "artifactId").map(_.text).headOption.getOrElse("")
  val version: String = (node \ "version").map(_.text).headOption.getOrElse("")
  val versioning: Versioning = (root \ "versioning").map(Versioning(_, this)).headOption.orNull

  private def compareCode(): Int = {
    val state = Seq(groupId, artifactId, version)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def compare(that: MetaData): Int = {
    compareCode().compare(that.compareCode())
  }
}

object MetaData {
  def apply(metaDataFile: File) = new MetaData(XML.loadFile(metaDataFile), Some(metaDataFile))

  def apply(metaData: String) = new MetaData(XML.loadString(metaData), None)

  def apply(metaData: InputStream) = new MetaData(XML.load(metaData), None)
}
