package de.visualdigits.dotgraph.core.`type`

import enumeratum._

sealed trait GraphType extends EnumEntry

object GraphType extends Enum[GraphType] {

  val values: IndexedSeq[GraphType] = findValues

  case object graph extends GraphType
  case object digraph extends GraphType
  case object subgraph extends GraphType
}
