package de.visualdigits.dotgraph.core.`type`

import enumeratum._

sealed trait ClusterMode extends EnumEntry

object ClusterMode extends Enum[ClusterMode] {

  val values: IndexedSeq[ClusterMode] = findValues

  case object global extends ClusterMode
  case object local extends ClusterMode
  case object none extends ClusterMode
}
