package de.visualdigits.dotgraph.core.entity.html

import de.visualdigits.dotgraph.core.attribute.AbstractDotAttributes
import de.visualdigits.dotgraph.core.entity.AbstractDotEntity

/**
 * Base class for all HTML like entities used for dot labels.
 *
 * @param tagName The tag which represents this entity in HTML like markup.
 * @param close Determines whether the tag should be closed or not.
 * @tparam D The node type.
 * @tparam T The corresponding attribute type for the node.
 */
abstract class AbstractHtmlEntity[D <: AbstractDotEntity[D, T], T <: AbstractDotAttributes[D, T]](tagName: String, close: Boolean = false) extends AbstractDotEntity[D, T] {

  override def toString: String = {
    val s = "<" + this.tagName + (if (!this.attributes.isEmpty) " " + this.attributes
    else "") + (if (this.close) "/"
    else "") + ">"
    s
  }
}
