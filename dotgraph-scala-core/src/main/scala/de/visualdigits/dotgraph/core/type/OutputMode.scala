package de.visualdigits.dotgraph.core.`type`

import enumeratum._

sealed trait OutputMode extends EnumEntry

object OutputMode extends Enum[OutputMode] {

  val values: IndexedSeq[OutputMode] = findValues

  case object breadthfirst extends OutputMode
  case object nodesfirst extends OutputMode
  case object edgesfirst extends OutputMode
}
