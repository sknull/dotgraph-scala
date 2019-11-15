package de.visualdigits.dotgraph.core.`type`

import enumeratum._

sealed trait ArrowType extends EnumEntry

object ArrowType extends Enum[ArrowType] {

  val values: IndexedSeq[ArrowType] = findValues

  case object box extends ArrowType
  case object circle extends ArrowType
  case object crow extends ArrowType
  case object diamond extends ArrowType
  case object dot extends ArrowType
  case object ediamond extends ArrowType
  case object empty extends ArrowType
  case object halfopen extends ArrowType
  case object inv extends ArrowType
  case object invdot extends ArrowType
  case object invempty extends ArrowType
  case object invodot extends ArrowType
  case object none extends ArrowType
  case object normal extends ArrowType
  case object obox extends ArrowType
  case object odiamond extends ArrowType
  case object odot extends ArrowType
  case object open extends ArrowType
  case object tee extends ArrowType
  case object vee extends ArrowType
}
