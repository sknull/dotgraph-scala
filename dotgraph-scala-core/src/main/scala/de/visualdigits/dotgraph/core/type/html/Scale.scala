package de.visualdigits.dotgraph.core.`type`.html

import enumeratum.{Enum, EnumEntry}


sealed abstract class Scale(override val entryName: String) extends EnumEntry

object Scale extends Enum[Scale] {

  val values: IndexedSeq[Scale] = findValues

  case object yes extends Scale("true")
  case object no extends Scale("false")
  case object width extends Scale("width")
  case object height extends Scale("height")
  case object both extends Scale("both")
}
