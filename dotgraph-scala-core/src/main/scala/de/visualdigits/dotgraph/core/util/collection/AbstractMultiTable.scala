package de.visualdigits.dotgraph.core.util.collection

import scala.collection.mutable

/**
 * Base class for collections which map two keys (row and column) to many values.
 *
 * @tparam R The row type.
 * @tparam C The column type.
 * @tparam V The value type.
 * @tparam S The collection type for the values.
 */
abstract class AbstractMultiTable[R, C, V, S <: mutable.Growable[V]] extends Table[R, C, S] {

  def addOneValue(rowKey: R, columnKey: C, value: V): Option[V] = {
    val row = getRow(rowKey).orElse(Some(createRow))
    val r = row.get
    val cell = r.getOrElse(columnKey, createCell)
    r.put(columnKey, cell)
    var old: Option[V] = Option.empty
    cell match {
      case value: mutable.Set[V] =>
        old = value.headOption
      case value: mutable.Buffer[V] =>
        old = value.headOption
      case _ =>
    }
    cell.addOne(value)
    table.put(rowKey, r)
    old
  }

  def addAll(rowKey: R, columnKey: C, elems: S): Option[mutable.Map[C, S]] = {
    val row = getRow(rowKey).orElse(Some(createRow))
    val r = row.get
    r.put(columnKey, elems)
    table.put(rowKey, r)
    row
  }

  override def size(): Int = table.values.map(_.values.map(_.knownSize).sum).sum
}
