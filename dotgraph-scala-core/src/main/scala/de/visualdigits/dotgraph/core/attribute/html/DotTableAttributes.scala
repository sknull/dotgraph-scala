package de.visualdigits.dotgraph.core.attribute.html

import de.visualdigits.dotgraph.core.`type`.PortPos
import de.visualdigits.dotgraph.core.`type`.html.{Align, VAlign}
import de.visualdigits.dotgraph.core.attribute.AbstractDotAttributes
import de.visualdigits.dotgraph.core.entity.DotColor
import de.visualdigits.dotgraph.core.entity.html.DotTable

/**
 * Attributes for a table as described
 * <a  href="https://graphviz.gitlab.io/_pages/doc/info/shapes.html#html">here</a>
 *
 * @param entity The entity to be returned from the fluent calls (see examples).
 */
class DotTableAttributes(entity: DotTable) extends AbstractDotAttributes[DotTable, DotTableAttributes](entity) {

  override def toString: String = {
    val s = super.repr(" ", true)
    s
  }

  def getAlign: Align = this.attributes.get("align").map(_.asInstanceOf[Align]).orNull

  def setAlign(align: Align): DotTableAttributes = {
    this.attributes.put("align", align)
    this
  }

  def getBgcolor: DotColor = this.attributes.get("bgcolor").map(_.asInstanceOf[DotColor]).orNull

  def setBgcolor(bgcolor: DotColor): DotTableAttributes = {
    this.attributes.put("bgcolor", bgcolor)
    this
  }

  def getBorder: Integer = this.attributes.get("border").asInstanceOf[Int]

  def setBorder(border: Integer): DotTableAttributes = {
    this.attributes.put("border", border)
    this
  }

  def getCellborder: Integer = this.attributes.get("cellborder").asInstanceOf[Int]

  def setCellborder(cellborder: Integer): DotTableAttributes = {
    this.attributes.put("cellborder", cellborder)
    this
  }

  def getCellpadding: Integer = this.attributes.get("cellpadding").asInstanceOf[Int]

  def setCellpadding(cellpadding: Integer): DotTableAttributes = {
    this.attributes.put("cellpadding", cellpadding)
    this
  }

  def getCellspacing: Integer = this.attributes.get("cellspacing").asInstanceOf[Int]

  def setCellspacing(cellspacing: Integer): DotTableAttributes = {
    this.attributes.put("cellspacing", cellspacing)
    this
  }

  def getColor: DotColor = this.attributes.get("color").map(_.asInstanceOf[DotColor]).orNull

  def setColor(color: DotColor): DotTableAttributes = {
    this.attributes.put("color", color)
    this
  }

  def getColumns: Integer = this.attributes.get("columns").asInstanceOf[Int]

  def setColumns(columns: Integer): DotTableAttributes = {
    this.attributes.put("columns", columns)
    this
  }

  def isFixedsize: Boolean = this.attributes.get("fixedsize").asInstanceOf[Boolean]

  def setFixedsize(fixedsize: Boolean): DotTableAttributes = {
    this.attributes.put("fixedsize", fixedsize)
    this
  }

  def getGradientangle: Double = this.attributes.get("gradientangle").asInstanceOf[Double]

  def setGradientangle(gradientangle: Double): DotTableAttributes = {
    this.attributes.put("gradientangle", gradientangle)
    this
  }

  def getHeight: Integer = this.attributes.get("height").asInstanceOf[Int]

  def setHeight(height: Integer): DotTableAttributes = {
    this.attributes.put("height", height)
    this
  }

  def getHref: String = this.attributes.get("href").asInstanceOf[String]

  def setHref(href: String): DotTableAttributes = {
    this.attributes.put("href", href)
    this
  }

  def getId: String = this.attributes.get("id").asInstanceOf[String]

  def setId(id: String): DotTableAttributes = {
    this.attributes.put("id", id)
    this
  }

  def getPort: PortPos = this.attributes.get("port").map(_.asInstanceOf[PortPos]).orNull

  def setPort(port: PortPos): DotTableAttributes = {
    this.attributes.put("port", port)
    this
  }

  def getRows: Integer = this.attributes.get("rows").asInstanceOf[Int]

  def setRows(rows: Integer): DotTableAttributes = {
    this.attributes.put("rows", rows)
    this
  }

  def getSides: Integer = this.attributes.get("sides").asInstanceOf[Int]

  def setSides(sides: Integer): DotTableAttributes = {
    this.attributes.put("sides", sides)
    this
  }

  def getStyle: String = this.attributes.get("style").asInstanceOf[String]

  def setStyle(style: String): DotTableAttributes = {
    this.attributes.put("style", style)
    this
  }

  def getTarget: String = this.attributes.get("target").asInstanceOf[String]

  def setTarget(target: String): DotTableAttributes = {
    this.attributes.put("target", target)
    this
  }

  def getTitle: String = this.attributes.get("title").asInstanceOf[String]

  def setTitle(title: String): DotTableAttributes = {
    this.attributes.put("title", title)
    this
  }

  def getTooltip: String = this.attributes.get("tooltip").asInstanceOf[String]

  def setTooltip(tooltip: String): DotTableAttributes = {
    this.attributes.put("tooltip", tooltip)
    this
  }

  def getValign: VAlign = this.attributes.get("valign").map(_.asInstanceOf[VAlign]).orNull

  def setValign(valign: VAlign): DotTableAttributes = {
    this.attributes.put("valign", valign)
    this
  }

  def getWidth: String = this.attributes.get("width").asInstanceOf[String]

  def setWidth(width: String): DotTableAttributes = {
    this.attributes.put("width", width)
    this
  }
}

object DotTableAttributes {
  def apply(entity: DotTable) = new DotTableAttributes(entity)
}
