package de.visualdigits.dotgraph.core.`type`

import enumeratum._

sealed trait EdgeStyle extends EnumEntry

object EdgeStyle extends Enum[EdgeStyle] {

  val values: IndexedSeq[EdgeStyle] = findValues

  case object olid extends EdgeStyle
  case object ashedEdgeStyle
  case object ottedEdgeStyle
  case object oldEdgeStyle
}
