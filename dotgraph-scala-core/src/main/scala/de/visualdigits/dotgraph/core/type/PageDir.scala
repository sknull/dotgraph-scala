package de.visualdigits.dotgraph.core.`type`

import enumeratum._

sealed trait PageDir extends EnumEntry

object PageDir extends Enum[PageDir] {

  val values: IndexedSeq[PageDir] = findValues

  case object BL extends PageDir
  case object BR extends PageDir
  case object TL extends PageDir
  case object TR extends PageDir
  case object RB extends PageDir
  case object RT extends PageDir
  case object LB extends PageDir
  case object LT extends PageDir
}
