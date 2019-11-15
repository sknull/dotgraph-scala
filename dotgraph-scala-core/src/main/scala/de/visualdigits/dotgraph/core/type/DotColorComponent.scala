package de.visualdigits.dotgraph.core.`type`

import enumeratum._

sealed trait DotColorComponent extends EnumEntry

/**
 * Determines which component of a given color to modify.
 */
object DotColorComponent extends Enum[DotColorComponent] {

  val values: IndexedSeq[DotColorComponent] = findValues

  case object RED extends DotColorComponent
  case object GREEN extends DotColorComponent
  case object BLUE extends DotColorComponent
  case object HUE extends DotColorComponent
  case object SATURATION extends DotColorComponent
  case object BRIGHTNESS extends DotColorComponent
}
