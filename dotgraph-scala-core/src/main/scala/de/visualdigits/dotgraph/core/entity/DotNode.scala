package de.visualdigits.dotgraph.core.entity

import de.visualdigits.dotgraph.core.`type`.{DotColorComponent, NodeStyle}
import de.visualdigits.dotgraph.core.attribute.DotNodeAttributes

import scala.collection.mutable

/**
 * A node of the graph.
 *
 * @param graph The containing graph.
 * @param id The id of this node.
 */
class DotNode(var graph: DotGraph, var id: String = "", var category: Option[String] = Option.empty) extends AbstractDotEntity[DotNode, DotNodeAttributes] with Ordered[DotNode] {

  override var attributes = new DotNodeAttributes(this)

  var partOfCycle: Boolean = false
  var routes: mutable.LinkedHashSet[mutable.ListBuffer[DotNode]] =  mutable.LinkedHashSet[mutable.ListBuffer[DotNode]]()
  var edges: mutable.Set[DotEdge] =  mutable.TreeSet[DotEdge]()
  var references: mutable.Set[DotEdge] =  mutable.TreeSet[DotEdge]()

  override def toString: String = {
    val sb: StringBuilder = new StringBuilder()
    if (id.nonEmpty) {
      sb.append("\"").append(id).append("\"")
    } else {
      sb.append("node")
    }
    sb.append(" [")
    if (attributes.nonEmpty) {
      val s = attributes.toString
      sb.append(s)
    }
    sb.append("];")
    if (edges.nonEmpty) sb.append("\n  ").append(edges.map(_.toString).mkString("\n  "))
    sb.append("\n")
    sb.toString()
  }

  /**
   * Performs a depth first search on the given node.
   *
   * @param node The node to start with.
   * @param dfsSet  List to store visited nodes.
   */
  def dfs(node: DotNode = this, dfsSet: mutable.LinkedHashSet[DotNode] = mutable.LinkedHashSet[DotNode]()): mutable.LinkedHashSet[DotNode] = {
    if (!graph.visited.contains(node)) {
      graph.visited.add(node)
      node.references.foreach(reference => dfs(reference.from, dfsSet))
      dfsSet.add(node)
    }
    dfsSet
  }

  /**
   * Performs a depth first search on the given node.
   *
   * @param node     The node to start with.
   * @param rootLineList List to store visited nodes.
   */
  def rootLine(node: DotNode = this, rootLineList: mutable.LinkedHashSet[DotNode] = mutable.LinkedHashSet[DotNode]()): mutable.LinkedHashSet[DotNode] = {
    if (!graph.visited.contains(node)) {
      graph.visited.add(node)
      node.edges.foreach(edge => rootLine(edge.to, rootLineList))
      rootLineList.add(node)
    }
    rootLineList
  }

  /**
   * Calculates all possible routes to other nodes sourcing from this one.
   *
   * @param route The source node.
   * @param level The recursion level used to cut off at a certain level.
   */
  private[entity] def determineRoutes(node: DotNode = this, route: mutable.ListBuffer[DotNode] = mutable.ListBuffer[DotNode](), level: Int = 0): Unit = {
    if (!graph.visited.contains(node)) {
      graph.visited.add(node)
      // add myself to list of visited nodes within this travel
      val r = mutable.ListBuffer(node) ++ route
      route.clear()
      route.addAll(r)
      // determine routes recursively
      node.references.foreach(reference => {
        determineRoutes(reference.from, route.clone(), level + 1)
      })
      // ### START OF THE BACKPATH FROM THE RECURSION ###
      node.addRoute(route)
      processRoutes(node)
    }
  }

  /**
   * Process routes for this node.
   *
   * @return boolean
   */
  private def processRoutes(node: DotNode): Unit = {
    node.references.foreach(reference => {
      val selfRoutes = mutable.LinkedHashSet[mutable.ListBuffer[DotNode]]()
      val from = reference.from
      val fromRoutes = from.routes
      val nodeRoutes = node.routes
      nodeRoutes.foreach(route => {
        val routeFrom = route.head
        val routeTo = route.last
        val r = mutable.ListBuffer(from) ++ route.clone()
        // avoid concurrent modification
        // !! DO NOT CONSOLIDATE WITH BLOCK BELOW - cycles will not be detected anymore (why is this?) !!
        if (nodeRoutes != fromRoutes) {
          fromRoutes.add(r)
        }
        else if (from == node) {
          selfRoutes.add(r)
        }
        else {
          return
        }
        // bailout from recursion when we face a self reference or have seen this route already
        if (from == node || from == routeFrom || from == routeTo) return
      })
      fromRoutes.addAll(selfRoutes)
    })
  }

  /**
   * Processes all routes of the given node which lead to the node itself (and hence build a cycle).
   */
  private[entity] def processCyclicRoutes(): Unit = {
    val routesTo = graph.getRoutesTo(this, this)
    routesTo.foreach(route => {
      route.foreach((node: DotNode) => {
        node.partOfCycle = true
        node.attributes.setColor(graph.cycleColor)
        node.processCyclicEdges()
        node.processCyclicReferences()
      })
    })
  }

  /**
   * Marks all cyclic edges.
   */
  private def processCyclicEdges(): Unit = {
    edges.filter((edge: DotEdge) => edge.to.partOfCycle).foreach((edge: DotEdge) => {
      val transitive = edge.transitive
      edge.attributes.setColor(if (transitive) graph.cycleColor.multiply(DotColorComponent.SATURATION, graph.factorTransitive)
      else graph.cycleColor)
    })
  }

  /**
   * marks all cyclic nodes.
   */
  private def processCyclicReferences(): Unit = {
    references.filter((reference: DotEdge) => reference.from.partOfCycle).foreach((reference: DotEdge) => {
      val transitive = reference.transitive
      reference.attributes.setColor(if (transitive) graph.cycleColor.multiply(DotColorComponent.SATURATION, graph.factorTransitive)
      else graph.cycleColor)
    })
  }

  /**
   * Sets the background color to the given one and the style to filled.
   *
   * @param bgcolor The color.
   * @return T
   */
  def setBgcolor(bgcolor: DotColor): DotNode = {
    attributes.setFillcolor(bgcolor).setStyle(NodeStyle.filled)
    this
  }

  def getBgColor: DotColor = attributes.getFillcolor

  def isRoot: Boolean = {
    val edges:  mutable.Set[DotEdge] = this.edges
    val references:  mutable.Set[DotEdge] = this.references
    edges.isEmpty && references.nonEmpty
  }

  def isLeaf: Boolean = {
    val edges:  mutable.Set[DotEdge] = this.edges
    val references:  mutable.Set[DotEdge] = this.references
    references.isEmpty && edges.nonEmpty
  }

  def isOrphan: Boolean = {
    val edges:  mutable.Set[DotEdge] = this.edges
    val references:  mutable.Set[DotEdge] = this.references
    edges.isEmpty && references.isEmpty
  }

  def removeEdges(to: DotNode):  Set[DotEdge] = {
    val edges:  Set[DotEdge] = getEdges(to)
    edges.foreach((edge: DotEdge) => this.edges.remove(edge))
    edges
  }

  def addEdge(to: DotNode, label: String = ""): DotEdge = {
    var edges = getEdges(to)
    if (edges.nonEmpty && label.nonEmpty) edges = edges.filter((it: DotEdge) => label == it.label)
    val edge = edges.headOption.getOrElse(createEdge(graph,this, to, label))
    this.edges += edge
    edge
  }

  /**
   * Returns whether the given edge is contained in this node.
   *
   * @param to The target node
   * @return boolean
   */
  def containsEdge(to: DotNode): Boolean = to != null && this.getEdges(to).nonEmpty

  /**
   * Returns a collection of edges pointing from this node to the given node.
   *
   * @param to The target node
   * @return Set<T>
   */
  def getEdges(to: DotNode):  Set[DotEdge] = {
    edges.filter((it: DotEdge) => to == it.to).toSet
  }

  def removeReferences(from: DotNode):  Set[DotEdge] = {
    val references:  mutable.Set[DotEdge] = getReferences(from)
    references.foreach((edge: DotEdge) => this.references.remove(edge))
    references.toSet
  }

  def addReference(from: DotNode, label: String = ""): DotEdge = {
    var references = getReferences(from)
    if (references.nonEmpty && label.nonEmpty) references = references.filter((it: DotEdge) => label == it.label)
    var reference = references.headOption.getOrElse(createEdge(graph,from, this, label))
    this.references += reference
    reference
  }

  /**
   * Returns whether the given edge is contained in this node.
   *
   * @param from The target node
   * @return boolean
   */
  def containsReference(from: DotNode): Boolean = from != null && this.getReferences(from).nonEmpty

  /**
   * Returns a collection of edges pointing from this node to the given node.
   *
   * @param from The target node
   * @return Set<T>
   */
  def getReferences(from: DotNode):  mutable.Set[DotEdge] = {
    references.filter((it: DotEdge) => from == it.from)
  }

  def addRoute(route: mutable.ListBuffer[DotNode]): Unit = {
    routes.add(route)
  }

  def createEdge(graph: DotGraph, from: DotNode, to: DotNode, label: String): DotEdge = {
    DotEdge(graph, from, to, label)
  }

  override def compare(that: DotNode): Int = id.compareTo(that.id)

  def canEqual(other: Any): Boolean = other.isInstanceOf[DotNode]

  override def equals(other: Any): Boolean = other match {
    case that: DotNode =>
      (that canEqual this) &&
        id == that.id
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(id)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def initEntity(): Unit = {}
}

object DotNode {
  def apply(graph: DotGraph, id: String = "", category: Option[String] = Option.empty) = new DotNode(graph, id, category)
}
