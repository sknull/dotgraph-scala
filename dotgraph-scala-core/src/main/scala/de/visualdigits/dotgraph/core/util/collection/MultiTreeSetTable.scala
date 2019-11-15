package de.visualdigits.dotgraph.core.util.collection

import scala.collection.mutable

/**
 * Collection which maps two keys to multiple unique values in a LinkedHashSet,
 * hence the order of insertion is retained.
 *
 * @param rowOrder Implicit for row ordering.
 * @param columnOrder Implicit for column ordering.
 * @tparam R The row type.
 * @tparam C The column type.
 * @tparam V The value type.
 */
class MultiTreeSetTable[R, C, V]()(implicit val rowOrder: Ordering[R], val columnOrder: Ordering[C]) extends AbstractMultiTable[R, C, V, mutable.Set[V]] {

  override protected def createTable: mutable.Map[R, mutable.Map[C, mutable.Set[V]]] = mutable.TreeMap[R, mutable.Map[C, mutable.Set[V]]]()

  override protected def createRow: mutable.Map[C, mutable.Set[V]] = mutable.TreeMap[C, mutable.Set[V]]()

  override protected def createCell: mutable.Set[V] = mutable.Set[V]()
}

object MultiTreeSetTable {
  def apply[R, C, V]()(implicit rowOrder: Ordering[R], columnOrder: Ordering[C]) = new MultiTreeSetTable[R, C, V]()(rowOrder, columnOrder)
}
