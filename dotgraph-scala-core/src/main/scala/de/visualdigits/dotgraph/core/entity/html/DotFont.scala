package de.visualdigits.dotgraph.core.entity.html

import de.visualdigits.dotgraph.core.attribute.html.DotFontAttributes
import de.visualdigits.dotgraph.core.entity.DotColor

/**
 * A HTML like font tag.
 */
class DotFont extends AbstractHtmlEntity[DotFont, DotFontAttributes]("font", false) with Label {

  override var attributes: DotFontAttributes = DotFontAttributes(this)

  var label: Label = _

  override def toString: String = {
    val s = super.toString + this.label + "</font>"
    s
  }

  override def initEntity(): Unit = {}

  def setLabel(label: Any, fgColor: DotColor = DotColor.DEFAULT): DotFont = {
    label match {
      case label: String => this.label = TextLabel(label)
      case label: Label => this.label = label
      case _ =>
    }
    fgColor match {
      case DotColor.DEFAULT =>
      case _ => attributes.setColor(fgColor)
    }
    this
  }
}

object DotFont {
  def apply() = new DotFont()
}
