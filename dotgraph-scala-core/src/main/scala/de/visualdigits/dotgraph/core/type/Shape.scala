package de.visualdigits.dotgraph.core.`type`

import enumeratum._

sealed trait Shape extends EnumEntry

object Shape extends Enum[Shape] {

  val values: IndexedSeq[Shape] = findValues

  case object assembly extends Shape
  case object box extends Shape
  case object box3d extends Shape
  case object cds extends Shape
  case object circle extends Shape
  case object component extends Shape
  case object cylinder extends Shape
  case object diamond extends Shape
  case object doublecircle extends Shape
  case object doubleoctagon extends Shape
  case object egg extends Shape
  case object ellipse extends Shape
  case object fivepoverhang extends Shape
  case object folder extends Shape
  case object hexagon extends Shape
  case object house extends Shape
  case object insulator extends Shape
  case object invhouse extends Shape
  case object invtrapezium extends Shape
  case object invtriangle extends Shape
  case object larrow extends Shape
  case object lpromoter extends Shape
  case object Mcircle extends Shape
  case object Mdiamond extends Shape
  case object Msquare extends Shape
  case object none extends Shape
  case object note extends Shape
  case object noverhang extends Shape
  case object octagon extends Shape
  case object oval extends Shape
  case object parallelogram extends Shape
  case object pentagon extends Shape
  case object plain extends Shape
  case object plaintext extends Shape
  case object point extends Shape
  case object polygon extends Shape
  case object primersite extends Shape
  case object promoter extends Shape
  case object proteasesite extends Shape
  case object proteinstab extends Shape
  case object rarrow extends Shape
  case object rect extends Shape
  case object rectangle extends Shape
  case object restrictionsite extends Shape
  case object ribosite extends Shape
  case object rnastab extends Shape
  case object rpromoter extends Shape
  case object septagon extends Shape
  case object signature extends Shape
  case object square extends Shape
  case object star extends Shape
  case object tab extends Shape
  case object terminator extends Shape
  case object threepoverhang extends Shape
  case object trapezium extends Shape
  case object triangle extends Shape
  case object tripleoctagon extends Shape
  case object underline extends Shape
  case object utr extends Shape
}
