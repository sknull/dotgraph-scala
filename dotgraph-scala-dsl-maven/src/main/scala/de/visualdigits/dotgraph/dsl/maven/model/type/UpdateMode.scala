package de.visualdigits.dotgraph.dsl.maven.model.`type`

import enumeratum._

sealed trait UpdateMode extends EnumEntry

object UpdateMode extends Enum[UpdateMode] {

  val values: IndexedSeq[UpdateMode] = findValues

  case object none extends UpdateMode
  case object resolved extends UpdateMode
  case object management extends UpdateMode
}
