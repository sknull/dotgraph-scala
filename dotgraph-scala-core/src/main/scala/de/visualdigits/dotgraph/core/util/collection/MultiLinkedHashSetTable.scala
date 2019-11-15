package de.visualdigits.dotgraph.core.util.collection

import scala.collection.mutable

/**
 * Collection which maps two keys to multiple unique values in a LinkedHashSet,
 * hence the order of insertion is retained.
 *
 * @tparam R The row type.
 * @tparam C The column type.
 * @tparam V The value type.
 */
class MultiLinkedHashSetTable[R, C, V] extends AbstractMultiTable[R, C, V, mutable.LinkedHashSet[V]] {

  override protected def createTable: mutable.Map[R, mutable.Map[C, mutable.LinkedHashSet[V]]] = mutable.LinkedHashMap[R, mutable.Map[C, mutable.LinkedHashSet[V]]]()

  override protected def createRow: mutable.Map[C, mutable.LinkedHashSet[V]] = mutable.LinkedHashMap[C, mutable.LinkedHashSet[V]]()

  override protected def createCell: mutable.LinkedHashSet[V] = mutable.LinkedHashSet()
}

object MultiLinkedHashSetTable {
  def apply[R, C, V]() = new MultiLinkedHashSetTable[R, C, V]()
}
