package de.visualdigits.dotgraph.core.attribute

import de.visualdigits.dotgraph.core.`type`._
import de.visualdigits.dotgraph.core.entity.html.{DotTable, Label, TextLabel}
import de.visualdigits.dotgraph.core.entity.{DotColor, DotNode}

/**
 * Attributes for nodes as described
 * <a  href="https://graphviz.gitlab.io/_pages/doc/info/attrs.html">here</a>
 *
 * @param entity The entity to be returned from the fluent calls (see examples).
 */
class DotNodeAttributes(entity: DotNode) extends AbstractDotAttributes[DotNode, DotNodeAttributes](entity) {

  override def toString: String = super.repr(",")

  def getPenwidth: Double = this.attributes.get("penwidth").asInstanceOf[Double]

  def setPenwidth(penwidth: Double): DotNodeAttributes = {
    this.attributes.put("penwidth", penwidth)
    this
  }

  def getPos: Point = this.attributes.get("pos").map(_.asInstanceOf[Point]).orNull

  def setPos(pos: Point): DotNodeAttributes = {
    this.attributes.put("pos", pos)
    this
  }

  def getXlabel: String = this.attributes.get("xlabel").map(_.asInstanceOf[String]).orNull

  def setXlabel(xlabel: String): DotNodeAttributes = {
    this.attributes.put("xlabel", xlabel)
    this
  }

  def getColor: DotColor = this.attributes.get("color").map(_.asInstanceOf[DotColor]).orNull

  def setColor(color: DotColor): DotNodeAttributes = {
    this.attributes.put("color", color)
    this
  }

  def getColorscheme: String = this.attributes.get("colorscheme").map(_.asInstanceOf[String]).orNull

  def setColorscheme(colorscheme: String): DotNodeAttributes = {
    this.attributes.put("colorscheme", colorscheme)
    this
  }

  def getStyle: NodeStyle = this.attributes.get("style").map(_.asInstanceOf[NodeStyle]).orNull

  def setStyle(style: NodeStyle): DotNodeAttributes = {
    this.attributes.put("style", style)
    this
  }

  def getComment: String = this.attributes.get("comment").map(_.asInstanceOf[String]).orNull

  def setComment(comment: String): DotNodeAttributes = {
    this.attributes.put("comment", comment)
    this
  }

  def getFontcolor: DotColor = this.attributes.get("fontcolor").map(_.asInstanceOf[DotColor]).orNull

  def setFontcolor(fontcolor: DotColor): DotNodeAttributes = {
    this.attributes.put("fontcolor", fontcolor)
    this
  }

  def getFontname: String = this.attributes.get("fontname").map(_.asInstanceOf[String]).orNull

  def setFontname(fontname: String): DotNodeAttributes = {
    this.attributes.put("fontname", fontname)
    this
  }

  def getFontsize: Double = this.attributes.get("fontsize").asInstanceOf[Double]

  def setFontsize(fontsize: Double): DotNodeAttributes = {
    this.attributes.put("fontsize", fontsize)
    this
  }

  def getLabel: Label = this.attributes.get("label").map(_.asInstanceOf[Label]).orNull

  def setLabel(table: DotTable): DotNodeAttributes = {
    this.attributes.put("label", table)
    this.setShape(Shape.plaintext) // avoid cutoff of edges - see dot documentation

    this
  }

  def setLabel(label: String): DotNodeAttributes = {
    this.attributes.put("label", new TextLabel (label))
    this
  }

  def getTarget: String = this.attributes.get("target").map(_.asInstanceOf[String]).orNull

  def setTarget(target: String): DotNodeAttributes = {
    this.attributes.put("target", target)
    this
  }

  def getSortv: Integer = this.attributes.get("sortv").asInstanceOf[Int]

  def setSortv(sortv: Integer): DotNodeAttributes = {
    this.attributes.put("sortv", sortv)
    this
  }

  def isNojustify: Boolean = this.attributes.get("nojustify").asInstanceOf[Boolean]

  def setNojustify(nojustify: Boolean): DotNodeAttributes = {
    this.attributes.put("nojustify", nojustify)
    this
  }

  def getRoot: String = this.attributes.get("root").map(_.asInstanceOf[String]).orNull

  def setRoot(root: String): DotNodeAttributes = {
    this.attributes.put("root", root)
    this
  }

  def getDistortion: Double = this.attributes.get("distortion").asInstanceOf[Double]

  def setDistortion(distortion: Double): DotNodeAttributes = {
    this.attributes.put("distortion", distortion)
    this
  }

  def isFixedsize: Boolean = this.attributes.get("fixedsize").asInstanceOf[Boolean]

  def setFixedsize(fixedsize: Boolean): DotNodeAttributes = {
    this.attributes.put("fixedsize", fixedsize)
    this
  }

  def getHeight: Double = this.attributes.get("height").asInstanceOf[Double]

  def setHeight(height: Double): DotNodeAttributes = {
    this.attributes.put("height", height)
    this
  }

  def getImage: String = this.attributes.get("image").map(_.asInstanceOf[String]).orNull

  def setImage(image: String): DotNodeAttributes = {
    this.attributes.put("image", image)
    this
  }

  def isImagescale: Boolean = this.attributes.get("imagescale").asInstanceOf[Boolean]

  def setImagescale(imagescale: Boolean): DotNodeAttributes = {
    this.attributes.put("imagescale", imagescale)
    this
  }

  def getOrientation: Integer = this.attributes.get("orientation").asInstanceOf[Int]

  def setOrientation(orientation: Integer): DotNodeAttributes = {
    this.attributes.put("orientation", orientation)
    this
  }

  def isRegular: Boolean = this.attributes.get("regular").asInstanceOf[Boolean]

  def setRegular(regular: Boolean): DotNodeAttributes = {
    this.attributes.put("regular", regular)
    this
  }

  def getSamplepoints: Integer = this.attributes.get("samplepoints").asInstanceOf[Int]

  def setSamplepoints(samplepoints: Integer): DotNodeAttributes = {
    this.attributes.put("samplepoints", samplepoints)
    this
  }

  def getShape: Shape = this.attributes.get("shape").map(_.asInstanceOf[Shape]).orNull

  def setShape(shape: Shape): DotNodeAttributes = {
    this.attributes.put("shape", shape)
    this
  }

  def getShapefile: String = this.attributes.get("shapefile").map(_.asInstanceOf[String]).orNull

  def setShapefile(shapefile: String): DotNodeAttributes = {
    this.attributes.put("shapefile", shapefile)
    this
  }

  def getSides: Integer = this.attributes.get("sides").asInstanceOf[Int]

  def setSides(sides: Integer): DotNodeAttributes = {
    this.attributes.put("sides", sides)
    this
  }

  def getSkew: Double = this.attributes.get("skew").asInstanceOf[Double]

  def setSkew(skew: Double): DotNodeAttributes = {
    this.attributes.put("skew", skew)
    this
  }

  def getWidth: Double = this.attributes.get("width").asInstanceOf[Double]

  def setWidth(width: Double): DotNodeAttributes = {
    this.attributes.put("width", width)
    this
  }

  def getZ: Double = this.attributes.get("z").asInstanceOf[Double]

  def setZ(z: Double): DotNodeAttributes = {
    this.attributes.put("z", z)
    this
  }

  def getPeripheries: Integer = this.attributes.get("peripheries").asInstanceOf[Int]

  def setPeripheries(peripheries: Integer): DotNodeAttributes = {
    this.attributes.put("peripheries", peripheries)
    this
  }

  def getGradientangle: Integer = this.attributes.get("gradientangle").asInstanceOf[Int]

  def setGradientangle(gradientangle: Integer): DotNodeAttributes = {
    this.attributes.put("gradientangle", gradientangle)
    this
  }

  def getMargin: Double = this.attributes.get("margin").asInstanceOf[Double]

  def setMargin(margin: Double): DotNodeAttributes = {
    this.attributes.put("margin", margin)
    this
  }

  def getFillcolor: DotColor = this.attributes.get("fillcolor").map(_.asInstanceOf[DotColor]).orNull

  def setFillcolor(fillcolor: DotColor): DotNodeAttributes = {
    this.attributes.put("fillcolor", fillcolor)
    this
  }

  def getLabelloc: String = this.attributes.get("labelloc").map(_.asInstanceOf[String]).orNull

  def setLabelloc(labelloc: String): DotNodeAttributes = {
    this.attributes.put("labelloc", labelloc)
    this
  }
}

object DotNodeAttributes {
  def apply(entity: DotNode) = new DotNodeAttributes(entity)
}
