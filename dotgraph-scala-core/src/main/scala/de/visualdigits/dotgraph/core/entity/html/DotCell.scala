package de.visualdigits.dotgraph.core.entity.html

import de.visualdigits.dotgraph.core.`type`.html.Align
import de.visualdigits.dotgraph.core.attribute.html.DotCellAttributes
import de.visualdigits.dotgraph.core.entity.DotColor

/**
 * A HTML like table cell tag.
 *
 * @param id The optional id.
 * @param label The optional label, if given the table will contzain only one row containing one cell with the specified label.
 * @param fgColor The text color.
 * @param bgColor The fill color.
 * @param colSpan The column span.
 * @param align The text align.
 */
class DotCell(val id: String = "", val label: Any = null, val fgColor: DotColor = DotColor.DEFAULT, val bgColor: DotColor = DotColor.DEFAULT, val colSpan: Int = 1, align: Align = Align.left) extends AbstractHtmlEntity[DotCell, DotCellAttributes]("td", false) {

  override var attributes: DotCellAttributes = DotCellAttributes(this)

  if (!id.isEmpty) this.attributes.setId(id)

  var labelElem: Label = {
    attributes.setAlign(align)
    createLabel(label, fgColor, bgColor, colSpan, align)
  }

  override def toString: String = {
    val s = super.toString + this.labelElem + "</td>"
    s
  }

  def setLabel(label: Any, fgColor: DotColor = DotColor.DEFAULT, bgColor: DotColor = DotColor.DEFAULT, colSpan: Int = 1, align: Align = Align.left): DotCell = {
    this.labelElem = createLabel(label, fgColor, bgColor, colSpan, align)
    this
  }

  private def createLabel(label: Any, fgColor: DotColor = DotColor.DEFAULT, bgColor: DotColor = DotColor.DEFAULT, colSpan: Int = 1, align: Align = Align.left): Label = {
    if (bgColor != DotColor.DEFAULT) attributes
      .setBgcolor(bgColor)
    if (colSpan > 1) attributes.setColspan(colSpan)
    fgColor match {
      case DotColor.DEFAULT => label match {
        case label: String => TextLabel(label)
        case label: Label => label
        case _ => TextLabel("")
      }
      case _ => label match {
        case label: String => DotFont().setLabel(TextLabel(label), fgColor)
        case label: Label => DotFont().setLabel(label, fgColor)
        case _ => TextLabel("")
      }
    }
  }

  override def initEntity(): Unit = {}
}

object DotCell {
  def apply(id: String = "", label: Any = null, fgColor: DotColor = DotColor.DEFAULT, bgColor: DotColor = DotColor.DEFAULT, colSpan: Int = 1, align: Align = Align.left) = new DotCell(id, label, fgColor, bgColor, colSpan, align)
}
