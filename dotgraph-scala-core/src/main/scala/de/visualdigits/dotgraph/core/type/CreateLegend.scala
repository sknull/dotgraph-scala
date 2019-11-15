package de.visualdigits.dotgraph.core.`type`

import enumeratum._

sealed trait CreateLegend extends EnumEntry

object CreateLegend extends Enum[CreateLegend] {

  val values: IndexedSeq[CreateLegend] = findValues

  case object NONE extends CreateLegend
  case object MINIMAL extends CreateLegend
  case object FULL extends CreateLegend
}
