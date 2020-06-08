package de.visualdigits.dotgraph.core.xmlmodel

import scala.reflect.ClassTag
import scala.xml.{Node, XML}

class XmlNodeFactory[T <: XmlNode[R], R <: XmlNode[R]] {

  def create(node: Node, root: R)(implicit tagT: ClassTag[T], tagR: ClassTag[R]): T = {
    val classT = tagT.runtimeClass
    val classR = tagR.runtimeClass
    if (classT eq classR){
      val constructor = classT.getConstructor(classOf[Node])
      var xmlNode: T = constructor.newInstance(node).asInstanceOf[T]
      if (xmlNode.nsModified) {
        val s = xmlNode.toString
        xmlNode = constructor.newInstance(XML.loadString(s)).asInstanceOf[T]
      }
      xmlNode
    }
    else {
      val constructor = classT.getConstructor(classOf[Node], classR)
      var xmlNode: T = constructor.newInstance(node, root).asInstanceOf[T]
      if (xmlNode.nsModified) xmlNode = constructor.newInstance(XML.loadString(xmlNode.toString), root).asInstanceOf[T]
      xmlNode
    }
  }
}

object XmlNodeFactory {
  def apply[T <: XmlNode[R], R <: XmlNode[R]] = new XmlNodeFactory[T, R]()
}