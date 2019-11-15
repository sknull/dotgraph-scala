package de.visualdigits.dotgraph.core.`type`

import enumeratum._

sealed trait PortPos extends EnumEntry

object PortPos extends Enum[PortPos] {

  val values: IndexedSeq[PortPos] = findValues

  case object n extends PortPos
  case object ne extends PortPos
  case object e extends PortPos
  case object se extends PortPos
  case object s extends PortPos
  case object sw extends PortPos
  case object w extends PortPos
  case object nw extends PortPos
  case object c extends PortPos
  case object auto extends PortPos
}
