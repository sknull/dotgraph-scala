package de.visualdigits.dotgraph.core.attribute.html

import de.visualdigits.dotgraph.core.`type`.PortPos
import de.visualdigits.dotgraph.core.`type`.html.{Align, VAlign}
import de.visualdigits.dotgraph.core.attribute.AbstractDotAttributes
import de.visualdigits.dotgraph.core.entity.DotColor
import de.visualdigits.dotgraph.core.entity.html.DotCell

/**
 * Attributes for a table cell as described
 * <a  href="https://graphviz.gitlab.io/_pages/doc/info/shapes.html#html">here</a>
 *
 * @param entity The entity to be returned from the fluent calls (see examples).
 */
class DotCellAttributes(entity: DotCell) extends AbstractDotAttributes[DotCell, DotCellAttributes](entity) {

  override def toString: String = super.repr(" ", true)

  def getAlign: Align = this.attributes.get("align").map(_.asInstanceOf[Align]).orNull

  def setAlign(align: Align): DotCellAttributes = {
    this.attributes.put("align", align)
    this
  }

  def getBAlign: Align = this.attributes.get("balign").map(_.asInstanceOf[Align]).orNull

  def setBAlign(align: Align): DotCellAttributes = {
    this.attributes.put("balign", align)
    this
  }

  def getBgcolor: DotColor = this.attributes.get("bgcolor").map(_.asInstanceOf[DotColor]).orNull

  def setBgcolor(bgcolor: DotColor): DotCellAttributes = {
    this.attributes.put("bgcolor", bgcolor)
    this
  }

  def getBorder: Integer = this.attributes.get("border").asInstanceOf[Int]

  def setBorder(border: Integer): DotCellAttributes = {
    this.attributes.put("border", border)
    this
  }

  def getCellpadding: Integer = this.attributes.get("cellpadding").asInstanceOf[Int]

  def setCellpadding(cellpadding: Integer): DotCellAttributes = {
    this.attributes.put("cellpadding", cellpadding)
    this
  }

  def getCellspacing: Integer = this.attributes.get("cellspacing").asInstanceOf[Int]

  def setCellspacing(cellspacing: Integer): DotCellAttributes = {
    this.attributes.put("cellspacing", cellspacing)
    this
  }

  def getColor: DotColor = this.attributes.get("color").map(_.asInstanceOf[DotColor]).orNull

  def setColor(color: DotColor): DotCellAttributes = {
    this.attributes.put("color", color)
    this
  }

  def getColspan: Integer = this.attributes.get("colspan").asInstanceOf[Int]

  def setColspan(colspan: Integer): DotCellAttributes = {
    this.attributes.put("colspan", colspan)
    this
  }

  def isFixedsize: Boolean = this.attributes.get("fixedsize").asInstanceOf[Boolean]

  def setFixedsize(fixedsize: Boolean): DotCellAttributes = {
    this.attributes.put("fixedsize", fixedsize)
    this
  }

  def getGradientangle: Double = this.attributes.get("gradientangle").asInstanceOf[Double]

  def setGradientangle(gradientangle: Double): DotCellAttributes = {
    this.attributes.put("gradientangle", gradientangle)
    this
  }

  def getHeight: Integer = this.attributes.get("height").asInstanceOf[Int]

  def setHeight(height: Integer): DotCellAttributes = {
    this.attributes.put("height", height)
    this
  }

  def getHref: String = this.attributes.get("href").asInstanceOf[String]

  def setHref(href: String): DotCellAttributes = {
    this.attributes.put("href", href)
    this
  }

  def getId: String = this.attributes.get("id").asInstanceOf[String]

  def setId(id: String): DotCellAttributes = {
    this.attributes.put("id", id)
    this
  }

  def getPort: PortPos = this.attributes.get("port").map(_.asInstanceOf[PortPos]).orNull

  def setPort(port: PortPos): DotCellAttributes = {
    this.attributes.put("port", port)
    this
  }

  def getRowspan: Integer = this.attributes.get("rowspan").asInstanceOf[Int]

  def setRowspan(rowspan: Integer): DotCellAttributes = {
    this.attributes.put("rowspan", rowspan)
    this
  }

  def getSides: Integer = this.attributes.get("sides").asInstanceOf[Int]

  def setSides(sides: Integer): DotCellAttributes = {
    this.attributes.put("sides", sides)
    this
  }

  def getStyle: String = this.attributes.get("style").asInstanceOf[String]

  def setStyle(style: String): DotCellAttributes = {
    this.attributes.put("style", style)
    this
  }

  def getTarget: String = this.attributes.get("target").asInstanceOf[String]

  def setTarget(target: String): DotCellAttributes = {
    this.attributes.put("target", target)
    this
  }

  def getTitle: String = this.attributes.get("title").asInstanceOf[String]

  def setTitle(title: String): DotCellAttributes = {
    this.attributes.put("title", title)
    this
  }

  def getTooltip: String = this.attributes.get("tooltip").asInstanceOf[String]

  def setTooltip(tooltip: String): DotCellAttributes = {
    this.attributes.put("tooltip", tooltip)
    this
  }

  def getValign: VAlign = this.attributes.get("valign").map(_.asInstanceOf[VAlign]).orNull

  def setValign(valign: VAlign): DotCellAttributes = {
    this.attributes.put("valign", valign)
    this
  }

  def getWidth: String = this.attributes.get("width").asInstanceOf[String]

  def setWidth(width: String): DotCellAttributes = {
    this.attributes.put("width", width)
    this
  }
}

object DotCellAttributes {
  def apply(entity: DotCell) = new DotCellAttributes(entity)
}
