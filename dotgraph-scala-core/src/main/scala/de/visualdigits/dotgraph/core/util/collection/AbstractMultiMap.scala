package de.visualdigits.dotgraph.core.util.collection

import scala.collection.mutable

/**
 * Base class for collections which map two keys (row and column) to many values.
 *
 * @tparam K The key type.
 * @tparam V The value type.
 */
abstract class AbstractMultiMap[K, V] extends mutable.Iterable[(K, mutable.Buffer[V])] {

  private val map: mutable.Map[K, mutable.Buffer[V]] = createMap

  def get(key: K): Option[mutable.Buffer[V]] = map.get(key)

  override def toString(): String = map.toString()

  def addOne(key: K, value: V): Option[mutable.Buffer[V]] = {
    val old = get(key).map(_.toBuffer)
    var _row = get(key)
    var row: mutable.Buffer[V] = null
    if (_row.isDefined) row = _row.get
    else {
      row = createRow
      _row = Some(row)
    }
    row.addOne(value)
    map.addOne(key, row)
    old
  }

  def subtractOne(key: K): Option[mutable.Buffer[V]] = {
    val old = get(key)
    map.subtractOne(key)
    old
  }

  override def size: Int = {
    map.values.map(_.size).sum
  }

  def iterator: Iterator[(K, mutable.Buffer[V])] = {
    map.iterator
  }

  def valueIterator: Iterator[(K, V)] = {
    map.toList.flatMap(entry => entry._2.map(value => (entry._1, value))).iterator
  }

  def createMap: mutable.Map[K, mutable.Buffer[V]]

  def createRow: mutable.Buffer[V]
}
