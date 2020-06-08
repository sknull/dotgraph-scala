package de.visualdigits.dotgraph.dsl.maven.model.common

class PropertyValue(val value: String, var derived: Boolean = false) {

  override def toString: String = {
    value
  }

  override def clone(): PropertyValue = {
    PropertyValue(value, derived)
  }

  def isEmpty(): Boolean = {
    value.isEmpty
  }
}

object PropertyValue {
  def apply(value: String, derived: Boolean = false) = new PropertyValue(value, derived)
}
