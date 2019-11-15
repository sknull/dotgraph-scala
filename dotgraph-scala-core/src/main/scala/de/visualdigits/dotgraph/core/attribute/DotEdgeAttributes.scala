package de.visualdigits.dotgraph.core.attribute

import de.visualdigits.dotgraph.core.`type`._
import de.visualdigits.dotgraph.core.entity.{DotColor, DotEdge}

/**
 * Attributes for edges as described
 * <a  href="https://graphviz.gitlab.io/_pages/doc/info/attrs.html">here</a>
 *
 * @param entity The entity to be returned from the fluent calls (see examples).
 */
class DotEdgeAttributes(entity: DotEdge) extends AbstractDotAttributes[DotEdge, DotEdgeAttributes](entity) {

  override def toString: String = super.repr(",")

  def getDir: DirType = this.attributes.get("dir").map(_.asInstanceOf[DirType]).orNull

  def setDir(dir: DirType): DotEdgeAttributes = {
    this.attributes.put("dir", dir)
    this
  }

  def getPenwidth: Double = this.attributes.get("penwidth").asInstanceOf[Double]

  def setPenwidth(penwidth: Double): DotEdgeAttributes = {
    this.attributes.put("penwidth", penwidth)
    this
  }

  def getArrowhead: ArrowType = this.attributes.get("arrowhead").map(_.asInstanceOf[ArrowType]).orNull

  def setArrowhead(arrowhead: ArrowType): DotEdgeAttributes = {
    this.attributes.put("arrowhead", arrowhead)
    this
  }

  def getArrowsize: Double = this.attributes.get("arrowsize").asInstanceOf[Double]

  def setArrowsize(arrowsize: Double): DotEdgeAttributes = {
    this.attributes.put("arrowsize", arrowsize)
    this
  }

  def getArrowtail: ArrowType = this.attributes.get("arrowtail").map(_.asInstanceOf[ArrowType]).orNull

  def setArrowtail(arrowtail: ArrowType): DotEdgeAttributes = {
    this.attributes.put("arrowtail", arrowtail)
    this
  }

  def isDecorate: Boolean = this.attributes.get("decorate").asInstanceOf[Boolean]

  def setDecorate(decorate: Boolean): DotEdgeAttributes = {
    this.attributes.put("decorate", decorate)
    this
  }

  def isHeadclip: Boolean = this.attributes.get("headclip").asInstanceOf[Boolean]

  def setHeadclip(headclip: Boolean): DotEdgeAttributes = {
    this.attributes.put("headclip", headclip)
    this
  }

  def getHeadlabel: String = this.attributes.get("headlabel").map(_.asInstanceOf[String]).orNull

  def setHeadlabel(headlabel: String): DotEdgeAttributes = {
    this.attributes.put("headlabel", headlabel)
    this
  }

  def getHeadport: String = this.attributes.get("headport").map(_.asInstanceOf[String]).orNull

  def setHeadport(headport: String): DotEdgeAttributes = {
    this.attributes.put("headport", headport)
    this
  }

  def getLabelfontsize: Double = this.attributes.get("labelfontsize").asInstanceOf[Double]

  def setLabelfontsize(labelfontsize: Double): DotEdgeAttributes = {
    this.attributes.put("labelfontsize", labelfontsize)
    this
  }

  def getLen: Double = this.attributes.get("len").asInstanceOf[Double]

  def setLen(len: Double): DotEdgeAttributes = {
    this.attributes.put("len", len)
    this
  }

  def isTailclip: Boolean = this.attributes.get("tailclip").asInstanceOf[Boolean]

  def setTailclip(tailclip: Boolean): DotEdgeAttributes = {
    this.attributes.put("tailclip", tailclip)
    this
  }

  def getTaillabel: String = this.attributes.get("taillabel").map(_.asInstanceOf[String]).orNull

  def setTaillabel(taillabel: String): DotEdgeAttributes = {
    this.attributes.put("taillabel", taillabel)
    this
  }

  def getTailport: PortPos = this.attributes.get("tailport").map(_.asInstanceOf[PortPos]).orNull

  def setTailport(tailport: PortPos): DotEdgeAttributes = {
    this.attributes.put("tailport", tailport)
    this
  }

  def getWeight: Integer = this.attributes.get("weight").asInstanceOf[Int]

  def setWeight(weight: Int): DotEdgeAttributes = {
    this.attributes.put("weight", weight)
    this
  }

  def getPos: Point = this.attributes.get("pos").map(_.asInstanceOf[Point]).orNull

  def setPos(pos: Point): DotEdgeAttributes = {
    this.attributes.put("pos", pos)
    this
  }

  def getXlabel: String = this.attributes.get("xlabel").map(_.asInstanceOf[String]).orNull

  def setXlabel(xlabel: String): DotEdgeAttributes = {
    this.attributes.put("xlabel", xlabel)
    this
  }

  def getColor: DotColor = this.attributes.get("color").map(_.asInstanceOf[DotColor]).orNull

  def setColor(color: DotColor): DotEdgeAttributes = {
    this.attributes.put("color", color)
    this
  }

  def getColorscheme: String = this.attributes.get("colorscheme").map(_.asInstanceOf[String]).orNull

  def setColorscheme(colorscheme: String): DotEdgeAttributes = {
    this.attributes.put("colorscheme", colorscheme)
    this
  }

  def getStyle: EdgeStyle = this.attributes.get("style").map(_.asInstanceOf[EdgeStyle]).orNull

  def setStyle(style: EdgeStyle): DotEdgeAttributes = {
    this.attributes.put("style", style)
    this
  }

  def getComment: String = this.attributes.get("comment").map(_.asInstanceOf[String]).orNull

  def setComment(comment: String): DotEdgeAttributes = {
    this.attributes.put("comment", comment)
    this
  }

  def getFontcolor: DotColor = this.attributes.get("fontcolor").map(_.asInstanceOf[DotColor]).orNull

  def setFontcolor(fontcolor: DotColor): DotEdgeAttributes = {
    this.attributes.put("fontcolor", fontcolor)
    this
  }

  def getFontname: String = this.attributes.get("fontname").map(_.asInstanceOf[String]).orNull

  def setFontname(fontname: String): DotEdgeAttributes = {
    this.attributes.put("fontname", fontname)
    this
  }

  def getFontsize: Double = this.attributes.get("fontsize").asInstanceOf[Double]

  def setFontsize(fontsize: Double): DotEdgeAttributes = {
    this.attributes.put("fontsize", fontsize)
    this
  }

  def getLabel: String = this.attributes.get("label").map(_.asInstanceOf[String]).orNull

  def setLabel(label: String): DotEdgeAttributes = {
    this.attributes.put("label", label)
    this
  }

  def getTarget: String = this.attributes.get("target").map(_.asInstanceOf[String]).orNull

  def setTarget(target: String): DotEdgeAttributes = {
    this.attributes.put("target", target)
    this
  }

  def isNojustify: Boolean = this.attributes.get("nojustify").asInstanceOf[Boolean]

  def setNojustify(nojustify: Boolean): DotEdgeAttributes = {
    this.attributes.put("nojustify", nojustify)
    this
  }

  def getFillcolor: DotColor = this.attributes.get("fillcolor").map(_.asInstanceOf[DotColor]).orNull

  def setFillcolor(fillcolor: DotColor): DotEdgeAttributes = {
    this.attributes.put("fillcolor", fillcolor)
    this
  }
}

object DotEdgeAttributes {
  def apply(entity: DotEdge) = new DotEdgeAttributes(entity)
}
