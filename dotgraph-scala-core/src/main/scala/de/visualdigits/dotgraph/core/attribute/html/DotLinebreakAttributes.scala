package de.visualdigits.dotgraph.core.attribute.html

import de.visualdigits.dotgraph.core.`type`.html.Align
import de.visualdigits.dotgraph.core.attribute.AbstractDotAttributes
import de.visualdigits.dotgraph.core.entity.html.DotLinebreak

/**
 * Attributes for a table line break as described
 * <a  href="https://graphviz.gitlab.io/_pages/doc/info/shapes.html#html">here</a>
 *
 * @param entity The entity to be returned from the fluent calls (see examples).
 */
class DotLinebreakAttributes(entity: DotLinebreak) extends AbstractDotAttributes[DotLinebreak, DotLinebreakAttributes](entity) {

  override def toString: String = super.repr(" ", true)

  def getAlign: Align = this.attributes.get("align").asInstanceOf[Align]

  def setAlign(align: Align): DotLinebreakAttributes = {
    this.attributes.put("align", align)
    this
  }
}

object DotLinebreakAttributes {
  def apply(entity: DotLinebreak) = new DotLinebreakAttributes(entity)
}
