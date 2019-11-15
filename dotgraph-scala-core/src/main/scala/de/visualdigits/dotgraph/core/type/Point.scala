package de.visualdigits.dotgraph.core.`type`

class Point(x: Double, y: Double) {
  override def toString: String = x + "," + y
}

object Point {
  def apply(x: Double, y: Double) = new Point(x, y)
}