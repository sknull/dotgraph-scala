package de.visualdigits.dotgraph.core.attribute.html

import de.visualdigits.dotgraph.core.attribute.AbstractDotAttributes
import de.visualdigits.dotgraph.core.entity.DotColor
import de.visualdigits.dotgraph.core.entity.html.DotFont

/**
 * Attributes for a table font as described
 * <a  href="https://graphviz.gitlab.io/_pages/doc/info/shapes.html#html">here</a>
 *
 * @param entity The entity to be returned from the fluent calls (see examples).
 */
class DotFontAttributes(entity: DotFont) extends AbstractDotAttributes[DotFont, DotFontAttributes](entity) {

  override def toString: String = super.repr(" ", true)

  def getFont: String = this.attributes.get("font").asInstanceOf[String]

  def setFont(font: String): DotFontAttributes = {
    this.attributes.put("font", font)
    this
  }

  def getColor: DotColor = DotColor(this.attributes.get("color").asInstanceOf[String])

  def setColor(color: DotColor): DotFontAttributes = {
    this.attributes.put("color", color.toString)
    this
  }

  def getFace: String = this.attributes.get("face").asInstanceOf[String]

  def setFace(face: String): DotFontAttributes = {
    this.attributes.put("face", face)
    this
  }

  def getPointSize: String = this.attributes.get("point-size").asInstanceOf[String]

  def setPointSize(pointSize: String): DotFontAttributes = {
    this.attributes.put("point-size", pointSize)
    this
  }
}

object DotFontAttributes {
  def apply(entity: DotFont) = new DotFontAttributes(entity)
}
