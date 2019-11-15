package de.visualdigits.dotgraph.core.`type`

import enumeratum._

sealed trait NodeStyle extends EnumEntry

object NodeStyle extends Enum[NodeStyle] {

  val values: IndexedSeq[NodeStyle] = findValues

  case object bold extends NodeStyle
  case object dashed extends NodeStyle
  case object diagonals extends NodeStyle
  case object dotted extends NodeStyle
  case object filled extends NodeStyle
  case object rounded extends NodeStyle
  case object solid extends NodeStyle
  case object striped extends NodeStyle
  case object wedged extends NodeStyle
}
