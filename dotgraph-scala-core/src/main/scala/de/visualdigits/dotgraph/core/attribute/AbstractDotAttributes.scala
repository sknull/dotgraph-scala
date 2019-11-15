package de.visualdigits.dotgraph.core.attribute

import de.visualdigits.dotgraph.core.entity.AbstractDotEntity
import de.visualdigits.dotgraph.core.entity.html.DotTable
import enumeratum.EnumEntry

import scala.collection.mutable

/**
 * Base class for all attribute classes for nodes.
 *
 * @param entity The entity to be returned from the fluent calls (see examples).
 * @tparam D The node type.
 * @tparam T The corresponding attribute type for the node.
 */
abstract class AbstractDotAttributes[D <: AbstractDotEntity[D, T], T <: AbstractDotAttributes[D, T]](var entity: D) {

  protected val attributes: mutable.Map[String, Any] = mutable.TreeMap()

  def repr(separator: String, html: Boolean = false): String = {
    val entries = mutable.ListBuffer[String]()
    attributes.foreachEntry((k, v) => {
      if (!html && (v.isInstanceOf[EnumEntry] || v.isInstanceOf[DotTable])) {
        entries.addOne(k + "=" + v)
      } else {
        entries.addOne(k + "=\"" + v + "\"")
      }
    })
    entries.mkString(separator)
  }

  def addAll(attributes: T): Unit = this.attributes.addAll(attributes.attributes)

  def isEmpty: Boolean = attributes.isEmpty

  def nonEmpty: Boolean = attributes.nonEmpty

  def clear(): Unit = attributes.clear()
}
