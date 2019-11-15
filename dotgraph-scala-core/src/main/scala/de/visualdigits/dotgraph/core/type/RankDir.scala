package de.visualdigits.dotgraph.core.`type`

import enumeratum._

sealed trait RankDir extends EnumEntry

object RankDir extends Enum[RankDir] {

  val values: IndexedSeq[RankDir] = findValues

  case object TB extends RankDir
  case object LR extends RankDir
  case object BT extends RankDir
  case object RL extends RankDir
}
