package de.visualdigits.dotgraph.core.util.collection

import scala.collection.mutable

/**
 * Map which allows retrieval by key (get) and value (lookup).
 *
 * @tparam K Key type.
 * @tparam V Value type.
 */
class BiMap[K, V] extends BiMapHelper(mutable.Map[K, V](), mutable.Map[V, K]()) {
}

sealed class BiMapHelper[K, V](forward: mutable.Map[K, V], backward: mutable.Map[V, K]) extends mutable.Map[K, V] {

  def inverse = new BiMapHelper(backward, forward)

  def get(key: K): Option[V] = forward get key

  def lookup(key: V): Option[K] = backward get key

  def iterator: Iterator[(K, V)] = forward.iterator

  def addOne(kv: (K, V)): BiMapHelper.this.type = {
    forward.get(kv._1).foreach(backward.remove)
    forward.addOne(kv)
    backward.addOne(kv.swap)
    this
  }

  override def addAll(xs: IterableOnce[(K, V)]): BiMapHelper.this.type = {
    xs.iterator.foreach(x => {
      forward.addOne(x._1, x._2)
      backward.addOne(x._2, x._1)
    })
    this
  }

  override def subtractAll(xs: IterableOnce[K]): BiMapHelper.this.type = {
    xs.iterator.foreach(x => {
      forward.get(x).foreach(backward.remove)
      forward.subtractOne(x)
    })
    this
  }

  def subtractOne(key: K): BiMapHelper.this.type = {
    backward --= (forward get key)
    forward -= key
    this
  }

  override def empty: BiMapHelper[K, V] = {
    forward.empty
    backward.empty
    this
  }

  override def size: Int = forward.size
}

object BiMap {
  def apply[K, V](elems: (K, V)*): BiMap[K, V] = new BiMap[K, V] ++= elems
}
