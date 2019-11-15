package de.visualdigits.dotgraph.core.`type`.html

import enumeratum.{Enum, EnumEntry}


sealed trait Align extends EnumEntry

object Align extends Enum[Align] {

  val values: IndexedSeq[Align] = findValues

  case object center extends Align
  case object left extends Align
  case object right extends Align
}
