package de.visualdigits.dotgraph.core.entity.html

class TextLabel(label: String) extends Label {
  override def toString: String = label
}

/**
 * A HTML like text label.
 */
object TextLabel {
  def apply(label: String) = new TextLabel(label)
}
