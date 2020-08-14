package de.visualdigits.dotgraph.core.entity.html

import de.visualdigits.dotgraph.core.`type`.html.Align
import de.visualdigits.dotgraph.core.attribute.html.DotTableAttributes
import de.visualdigits.dotgraph.core.entity.DotColor

import scala.collection.mutable

/**
 * A HTML like table tag.
 */
class DotTable(id: String = "", row: Any = null, fgColor: DotColor = DotColor.DEFAULT, bgColor: DotColor = DotColor.DEFAULT, colSpan: Int = 1, align: Align = Align.left) extends AbstractHtmlEntity[DotTable, DotTableAttributes]("table", false) with mutable.Iterable[DotRow] {

  override var attributes: DotTableAttributes = DotTableAttributes(this)

  if (!id.isEmpty) this.attributes.setId(id)

  /** The list of rows. */
  private val rows = mutable.ListBuffer[DotRow]()
  addRow(row, fgColor, bgColor, colSpan, align)

  /** The explicit title row */
  private var title: DotRow = _

  /** The explicit header row (only rendered when the table contains rows). */
  private var header: DotRow = _

  override def toString: String = {
    val s = "<" + super[AbstractHtmlEntity].toString + (if (this.title != null) this.title.toString
    else "") + (if (this.header != null && this.rows.nonEmpty) this.header.toString
    else "") + rows.map(_.toString).mkString("") + "</table>>"
    s
  }

  def clearRows(): Unit = {
    rows.clear()
  }

  def addRow(row: Any = null, fgColor: DotColor = DotColor.DEFAULT, bgColor: DotColor = DotColor.DEFAULT, colSpan: Int = 1, align: Align = Align.left): DotTable = {
    this.rows.addAll(createRow(row, fgColor, bgColor, colSpan, align))
    this
  }

  private def createRow(row: Any = null, fgColor: DotColor = DotColor.DEFAULT, bgColor: DotColor = DotColor.DEFAULT, colSpan: Int = 1, align: Align = Align.left): Option[DotRow] = {
    row match {
      case label: String => Some(DotRow(label, fgColor, bgColor, colSpan, align))
      case cell: DotCell => Some(DotRow(cell))
      case row: DotRow => Some(row)
      case _ => Option.empty
    }
  }

  def setTitle(title: DotRow): DotTable = {
    this.title = title
    this
  }

  def setHeader(header: DotRow): Unit = {
    this.header = header
  }

  def getRow(index: Int): DotRow = this.rows.apply(index)

  override def initEntity(): Unit = {}

  override def iterator: Iterator[DotRow] = rows.iterator
}

object DotTable {
  def apply(id: String = "") = new DotTable(id)
}
