package de.visualdigits.dotgraph.core.util.collection

import scala.collection.mutable

/**
 * Tree based collection which maps a key to multiple sorted values.
 *
 * @tparam K The key type.
 * @tparam V The value type.
 */
class TreeMultiMap[K, V]()(implicit val keyOrder: Ordering[K]) extends AbstractMultiMap[K, V] {

  override def createMap: mutable.Map[K, mutable.Buffer[V]] = mutable.TreeMap[K, mutable.Buffer[V]]()

  override def createRow: mutable.Buffer[V] = new mutable.ListBuffer[V]
}

object TreeMultiMap {
  def apply[K, V]()(implicit keyOrder: Ordering[K]) = new TreeMultiMap[K, V]()(keyOrder)
}
