package de.visualdigits.dotgraph.core.xmlmodel

import java.io.StringWriter

import scala.collection.mutable
import scala.xml._
import scala.xml.transform.RewriteRule

class XmlNode[R <: XmlNode[R]](var node: Node, val rootNode: R) {

  var nsModified: Boolean = false

  val nameSpaces: Map[String, String] = {
    val urls: mutable.ListBuffer[String] = mutable.ListBuffer()
    val nameSpaces = getNamespaces(urls = urls)
    if (nameSpaces.isEmpty && urls.length == 1) {
      nsModified = true
      val url = urls.head
      setMainNamespace(url)
      getNamespaces(urls = urls)
    } else nameSpaces
  }

  override def toString: String = {
    val writer = new StringWriter()
    XML.write(writer, node, "utf-8", xmlDecl = false, null)
    writer.toString
  }

  private def getNamespaces(ns: NamespaceBinding = node.scope, map: mutable.Map[String, String] = mutable.Map(), urls: mutable.ListBuffer[String]): Map[String, String] = {
    val prefix = ns.prefix
    val url = ns.getURI(prefix)

    if (prefix != null && url != null) map.put(prefix, url)
    else if (url != null) {
      urls.append(url)
    }
    val parent = ns.parent
    if (parent!= null) getNamespaces(parent, map, urls)
    node.scope.prefix
    map.toMap
  }

  def setMainNamespace(uri: String): Unit = {
    val ns = NamespaceBinding("xml", uri, TopScope)
    object setNamespaceAndSchema extends RewriteRule {
      override def transform(n: Node): Seq[Node] = n match {
        case Elem(prefix, label, attribs, _, children @ _*)  =>
          Elem(prefix, label, attribs, ns, children.map(setNamespaceAndSchema) :_*)
        case other => other
      }
    }
    node = setNamespaceAndSchema(node)
  }
}
