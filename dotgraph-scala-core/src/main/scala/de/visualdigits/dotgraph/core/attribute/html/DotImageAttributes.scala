package de.visualdigits.dotgraph.core.attribute.html

import de.visualdigits.dotgraph.core.`type`.html.Scale
import de.visualdigits.dotgraph.core.attribute.AbstractDotAttributes
import de.visualdigits.dotgraph.core.entity.html.DotImage

/**
 * Attributes for a table image as described
 * <a  href="https://graphviz.gitlab.io/_pages/doc/info/shapes.html#html">here</a>
 *
 * @param entity The entity to be returned from the fluent calls (see examples).
 */
class DotImageAttributes(entity: DotImage) extends AbstractDotAttributes[DotImage, DotImageAttributes](entity) {

  override def toString: String = super.repr(" ", true)

  def getScale: Scale = this.attributes.get("scale").asInstanceOf[Scale]

  def setScale(scale: Scale): DotImageAttributes = {
    this.attributes.put("scale", scale)
    this
  }

  def getSrc: String = this.attributes.get("src").asInstanceOf[String]

  def setSrc(src: String): DotImageAttributes = {
    this.attributes.put("src", src)
    this
  }
}

object DotImageAttributes {
  def apply(entity: DotImage) = new DotImageAttributes(entity)
}
