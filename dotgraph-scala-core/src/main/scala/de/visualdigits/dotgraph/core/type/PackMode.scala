package de.visualdigits.dotgraph.core.`type`

import enumeratum._

sealed trait PackMode extends EnumEntry

object PackMode extends Enum[PackMode] {

  val values: IndexedSeq[PackMode] = findValues

  case object node extends PackMode
  case object clust extends PackMode
  case object graph extends PackMode
}
