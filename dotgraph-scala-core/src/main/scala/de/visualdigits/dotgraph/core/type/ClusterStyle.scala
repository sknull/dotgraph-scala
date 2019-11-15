package de.visualdigits.dotgraph.core.`type`

import enumeratum._

sealed trait ClusterStyle extends EnumEntry

object ClusterStyle extends Enum[ClusterStyle] {

  val values: IndexedSeq[ClusterStyle] = findValues

  case object bold extends ClusterStyle
  case object dashed extends ClusterStyle
  case object dotted extends ClusterStyle
  case object filled extends ClusterStyle
  case object rounded extends ClusterStyle
  case object solid extends ClusterStyle
  case object striped extends ClusterStyle
  case object wedged extends ClusterStyle
}
