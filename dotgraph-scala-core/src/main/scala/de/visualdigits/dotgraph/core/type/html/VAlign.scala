package de.visualdigits.dotgraph.core.`type`.html

import enumeratum.{Enum, EnumEntry}


sealed trait VAlign extends EnumEntry

object VAlign extends Enum[VAlign] {

  val values: IndexedSeq[VAlign] = findValues

  case object top extends VAlign
  case object middle extends VAlign
  case object bottom extends VAlign
}
