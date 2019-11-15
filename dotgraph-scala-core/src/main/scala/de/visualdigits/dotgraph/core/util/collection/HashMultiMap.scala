package de.visualdigits.dotgraph.core.util.collection

import scala.collection.mutable

/**
 * Hash based collection which maps a key to multiple unsorted values.
 *
 * @tparam K The key type.
 * @tparam V The value type.
 */
class HashMultiMap[K, V] extends AbstractMultiMap[K, V] {

  override def createMap: mutable.Map[K, mutable.Buffer[V]] = mutable.Map[K, mutable.Buffer[V]]()

  override def createRow: mutable.Buffer[V] = new mutable.ListBuffer[V]
}

object HashMultiMap {
  def apply[K, V]() = new HashMultiMap[K, V]()
}
