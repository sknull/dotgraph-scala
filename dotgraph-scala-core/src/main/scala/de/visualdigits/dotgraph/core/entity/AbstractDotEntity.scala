package de.visualdigits.dotgraph.core.entity

import de.visualdigits.dotgraph.core.attribute.AbstractDotAttributes

/**
 * Base class for all dot entities.
 *
 * @tparam D The node type.
 * @tparam T The corresponding attribute type for the node.
 */
abstract class AbstractDotEntity[D <: AbstractDotEntity[D, T], T <: AbstractDotAttributes[D, T]] {

  var attributes: T

  def initEntity(): Unit

  def clear(): Unit = attributes.clear()
}