package de.visualdigits.dotgraph.core.entity

import de.visualdigits.dotgraph.core.`type`.GraphType
import de.visualdigits.dotgraph.core.attribute.DotEdgeAttributes

/**
 * An edge in the graph.
 *
 * @param graph The containing graph.
 * @param from The source node of the edge.
 * @param to The destination node of the edge.
 * @param label The optional label of the edge.
 */
class DotEdge(val graph: DotGraph, val from: DotNode, val to: DotNode, val label: String = "") extends AbstractDotEntity[DotEdge, DotEdgeAttributes] with Ordered[DotEdge] {

  override var attributes: DotEdgeAttributes = {
    val attrs = new DotEdgeAttributes(this)
    attrs.setLabel(label)
    attrs
  }

  var transitive: Boolean = false

  override def toString: String = {
    val sb: StringBuilder = new StringBuilder
    sb.append("\"").append(from.id).append("\"")
    var graphType: GraphType = this.graph.graphType
    if (graphType eq GraphType.subgraph) {
      graphType = this.graph.getRootGraph.graphType
    }
    graphType match {
      case GraphType.digraph =>
        sb.append(" -> ")
      case GraphType.graph =>
        sb.append(" -- ")
      case _ =>
    }
    sb.append("\"").append(this.to.id).append("\" [")
    if (this.attributes.nonEmpty) {
      sb.append(this.attributes.toString)
    }
    sb.append("];")
    sb.toString
  }

  override def compare(that: DotEdge): Int = {
    var c: Int = from.compareTo(that.from)
    if (c == 0) c = to.compareTo(that.to)
    if (c == 0) c = label.compareTo(that.label)
    c
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[DotEdge]

  override def equals(other: Any): Boolean = other match {
    case that: DotEdge =>
      (that canEqual this) &&
        from == that.from &&
        to == that.to &&
        label == that.label
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(from, to, label)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def initEntity(): Unit = {}
}

object DotEdge {
  def apply(graph: DotGraph, from: DotNode, to: DotNode, label: String = "") = new DotEdge(graph, from, to, label)
}
