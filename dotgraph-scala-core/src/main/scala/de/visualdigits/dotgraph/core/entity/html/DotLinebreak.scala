package de.visualdigits.dotgraph.core.entity.html

import de.visualdigits.dotgraph.core.attribute.html.DotLinebreakAttributes

/**
 * A HTML like line break tag.
 */
class DotLinebreak extends AbstractHtmlEntity[DotLinebreak, DotLinebreakAttributes]("br", true) {

  override var attributes: DotLinebreakAttributes = DotLinebreakAttributes(this)

  override def initEntity(): Unit = {}
}

object DotLinebreak {
  def apply() = new DotLinebreak()
}
