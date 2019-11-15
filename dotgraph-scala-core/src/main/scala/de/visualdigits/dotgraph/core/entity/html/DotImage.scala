package de.visualdigits.dotgraph.core.entity.html

import de.visualdigits.dotgraph.core.attribute.html.DotImageAttributes

/**
 * A HTML like image tag.
 */
class DotImage extends AbstractHtmlEntity[DotImage, DotImageAttributes]("img", false) {

  override var attributes: DotImageAttributes = DotImageAttributes(this)

  override def initEntity(): Unit = {}
}

object DotImage {
  def apply() = new DotImage()
}
