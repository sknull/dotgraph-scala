package de.visualdigits.dotgraph.core.`type`

import enumeratum._

sealed trait RankType extends EnumEntry

object RankType extends Enum[RankType] {

  val values: IndexedSeq[RankType] = findValues

  case object same extends RankType
  case object min extends RankType
  case object source extends RankType
  case object max extends RankType
  case object sink extends RankType
}
