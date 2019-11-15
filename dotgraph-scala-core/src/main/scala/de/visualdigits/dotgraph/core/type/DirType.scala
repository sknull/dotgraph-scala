package de.visualdigits.dotgraph.core.`type`

import enumeratum._

sealed trait DirType extends EnumEntry

object DirType extends Enum[DirType] {

  val values: IndexedSeq[DirType] = findValues

  case object forward extends DirType
  case object back extends DirType
  case object both extends DirType
  case object none extends DirType
}
