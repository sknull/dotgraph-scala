package de.visualdigits.dotgraph.core.util.collection

import scala.collection.mutable

/**
 * Interface for collections which map two keys (row and column) to many values.
 *
 * @tparam R The row type.
 * @tparam C The column type.
 * @tparam V The value type.
 */
class Table[R, C, V] extends Iterable[(R, mutable.Map[C, V])] with mutable.Iterable[(R, mutable.Map[C, V])]{

  protected val table: mutable.Map[R, mutable.Map[C, V]] = createTable

  override def toString: String = table.toString()

  def get(rowKey: R, columnKey: C): Option[V] = table.getOrElse(rowKey, createRow).get(columnKey)

  def getRow(rowKey: R): Option[mutable.Map[C, V]] = table.get(rowKey)

  def addOne(rowKey: R, columnKey: C, value: V): Option[V] = {
    val row: Option[mutable.Map[C, V]] = getRow(rowKey).orElse(Some(createRow))
    val r = row.get
    val old = r.get(columnKey)
    r.put(columnKey, value)
    table.put(rowKey, r)
    old
  }

  def addAll(rowKey: R, values: Map[C, V]): Option[Map[C, V]] = table.put(rowKey, mutable.Map[C, V](values.toSeq:_*)).map(_.toMap)

  def remove(rowKey: R, columnKey: C): Option[V] = {
    val old = get(rowKey, columnKey)
    table.get(rowKey).map(_.remove(columnKey))
    old
  }

  def clear(): Unit = table.values.foreach(_.clear()); table.clear()

  override def size: Int = table.values.map(_.size).sum

  protected def createTable: mutable.Map[R, mutable.Map[C, V]] = mutable.Map[R, mutable.Map[C, V]]()

  protected def createCell: V = null.asInstanceOf[V]

  protected def createRow: mutable.Map[C, V] = mutable.Map[C, V]()

  override def iterator: Iterator[(R, mutable.Map[C, V])] = table.iterator
}

object Table {
  def apply[R, C, V]() = new Table[R, C, V]()
}
