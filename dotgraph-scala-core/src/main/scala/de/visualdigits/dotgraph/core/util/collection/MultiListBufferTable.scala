package de.visualdigits.dotgraph.core.util.collection

import scala.collection.mutable

/**
 * Collection which maps two keys to multiple non unique values in a ListBuffer,
 * hence the order of insertion is retained.
 *
 * @tparam R The row type.
 * @tparam C The column type.
 * @tparam V The value type.
 */
class MultiListBufferTable[R, C, V] extends AbstractMultiTable[R, C, V, mutable.ListBuffer[V]]  {

  override protected def createCell = mutable.ListBuffer[V]()
}

object MultiListBufferTable {
  def apply[R, C, V]() = new MultiListBufferTable[R, C, V]()
}
