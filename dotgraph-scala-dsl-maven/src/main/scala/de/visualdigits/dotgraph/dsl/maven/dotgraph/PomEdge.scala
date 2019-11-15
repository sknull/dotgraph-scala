package de.visualdigits.dotgraph.dsl.maven.dotgraph

import de.visualdigits.dotgraph.core.entity.{DotEdge, DotGraph, DotNode}

/**
 * A thin wrapper around DotEdge which adds a flag to determine if the edge points to the parent project.
 *
 * @param graph The containing graph.
 * @param from The source node of the edge.
 * @param to The destination node of the edge.
 * @param label The optional label of the edge.
 * @param isParent Determines whether the edge connects a project to its parent project or not.
 */
class PomEdge(graph: DotGraph, from: DotNode, to: DotNode, label: String = "", var isParent: Boolean = false) extends DotEdge(graph, from, to, label) {
}

object PomEdge {
  def apply(graph: DotGraph, from: DotNode, to: DotNode, label: String = "", isParent: Boolean = false) = new PomEdge(graph, from, to, label, isParent)
}