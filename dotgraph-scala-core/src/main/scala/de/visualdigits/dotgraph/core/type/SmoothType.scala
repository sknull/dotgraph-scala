package de.visualdigits.dotgraph.core.`type`

import enumeratum._

sealed trait SmoothType extends EnumEntry

object SmoothType extends Enum[SmoothType] {

  val values: IndexedSeq[SmoothType] = findValues

  case object none extends SmoothType
  case object avg_dist extends SmoothType
  case object graph_dist extends SmoothType
  case object power_dist extends SmoothType
  case object rng extends SmoothType
  case object spring extends SmoothType
  case object triangle extends SmoothType
}
