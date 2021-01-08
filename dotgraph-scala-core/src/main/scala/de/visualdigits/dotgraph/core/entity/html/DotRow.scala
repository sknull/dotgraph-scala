package de.visualdigits.dotgraph.core.entity.html

import de.visualdigits.dotgraph.core.`type`.html.Align
import de.visualdigits.dotgraph.core.entity.DotColor

import scala.collection.mutable

/**
 * A HTML like table row tag.
 */
class DotRow(cell: Any = null, fgColor: DotColor = DotColor.DEFAULT, bgColor: DotColor = DotColor.DEFAULT, colSpan: Int = 1, align: Align = Align.left) extends mutable.Iterable[DotCell] {

  /** The list of cells. */
  val cells = mutable.ListBuffer[DotCell]()
  addCell(cell, fgColor, bgColor, colSpan, align)

  override def toString: String = {
    "<tr>" + cells.map(_.toString).mkString("") + "</tr>"
  }

  def addCell(cell: Any = null, fgColor: DotColor = DotColor.DEFAULT, bgColor: DotColor = DotColor.DEFAULT, colSpan: Int = 1, align: Align = Align.left): DotRow = {
    this.cells.addAll(createCell(cell, fgColor, bgColor, colSpan, align))
    this
  }

  private def createCell(cell: Any = null, fgColor: DotColor = DotColor.DEFAULT, bgColor: DotColor = DotColor.DEFAULT, colSpan: Int = 1, align: Align = Align.left): Option[DotCell] = {
    cell match {
      case label: String => Some(DotCell("", label, fgColor, bgColor, colSpan, align))
      case cell: DotCell => Some(cell)
      case _ => Option.empty
    }
  }

  def getCell(index: Int): DotCell = this.cells.apply(index)

  override def iterator: Iterator[DotCell] = this.cells.iterator
}

object DotRow {
  def apply(cell: Any = null, fgColor: DotColor = DotColor.DEFAULT, bgColor: DotColor = DotColor.DEFAULT, colSpan: Int = 1, align: Align = Align.left) = new DotRow(cell, fgColor, bgColor, colSpan, align)
}
