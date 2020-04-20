package de.visualdigits.dotgraph.core.entity

import java.io.File
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.util.Date

import de.visualdigits.dotgraph.core.`type`._
import de.visualdigits.dotgraph.core.attribute.DotGraphAttributes
import de.visualdigits.dotgraph.core.entity.html.{DotCell, DotRow, DotTable, TextLabel}
import de.visualdigits.dotgraph.core.util.collection.MultiListBufferTable
import de.visualdigits.dotgraph.core.util.color.DistinctColorGoldenAngle
import de.visualdigits.dotgraph.core.util.exec.ShellCommand
import de.visualdigits.dotgraph.core.util.system.OsUtil

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * The dot graph.
 */
class DotGraph(var id: String, val isStrict: Boolean = false, val graphType: GraphType = GraphType.digraph) extends AbstractDotEntity[DotGraph, DotGraphAttributes] with Ordered[DotGraph] {

  override var attributes = DotGraphAttributes(this)

  /** All nodes which have no parent */
  val roots: mutable.Set[DotNode] = mutable.Set()

  /** All nodes which have no child */
  val leafs: mutable.Set[DotNode] = mutable.Set()

  /** Nodes without id are considered a template node - all following nodes will have the same attributes. */
  val templateNodes: mutable.Set[DotNode] = mutable.TreeSet()

  /** All known nodes within this graph. */
  val nodes: mutable.Map[String, DotNode] = mutable.TreeMap()

  /** All sub graphs. */
  val subGraphs: mutable.Map[String, DotGraph] = mutable.Map()

  /** The depth first search order - calculated along with the routes. */
  val dfsSet: mutable.Set[DotNode] = mutable.LinkedHashSet()

  /** All known routes within this graph. */
  val routes: mutable.Set[mutable.ListBuffer[DotNode]] = mutable.LinkedHashSet()

  /** All color categories for nodes within this graph. */
  val categories: mutable.Map[String, DotColor] = mutable.LinkedHashMap()

  /** Helper set to determine nodes already visited. */
  val visited: mutable.Set[DotNode] = mutable.Set()

  /** The parent graph (if any). */
  var parentGraph: DotGraph = _

  /** The type of legend to generate. */
  var legendType: CreateLegend = CreateLegend.MINIMAL

  /** Determines whether to calculate transitive edges or not while calculating routes. */
  var doDetermineTransitiveEdges: Boolean = false

  /** The color used for nodes and edges in cycles. */
  var cycleColor: DotColor = DotColor.RED

  var factorTransitive: Float = 0.3f

  // apply proper attributes for top level graphs.
  if (graphType != GraphType.subgraph) {
    attributes
      .setRankdir(RankDir.BT)
      .setSize(DotGraph.SIZE_A4)
      .setRatio(DotGraph.RATIO_AUTO)
  }

  override def toString: String = {
    val sb = new StringBuilder
    sb.append(graphType).append(" \"")
      .append(id).append("\" {\n")
    if (attributes.nonEmpty) sb.append("  ")
      .append(attributes.toString).append(";\n\n")
    if (nodes.nonEmpty) sb.append("  ")
      .append(nodes.values.map(_.toString)
        .mkString("\n  ")).append("\n\n")
    if (subGraphs.nonEmpty) {
      subGraphs.values.foreach(subgraph => {
        sb.append(subgraph.toString).append("\n")
      })
    }
    if (legendType != CreateLegend.NONE) sb.append("  ").append(createLegend.toString)
    sb.append("}\n")
    val s = sb.toString
    s
  }

  /**
   * Convenience method for fluent usage.
   *
   * @param legendType The legend type.
   * @return
   */
  def setCreateLegend(legendType: CreateLegend = CreateLegend.MINIMAL): DotGraph = {
    this.legendType = legendType
    this
  }

  /**
   * Convenience method for fluent usage.
   *
   * @param cycleColor The color used for nodes and edges in cycles.
   * @return
   */
  def setCycleColors(cycleColor: DotColor): DotGraph = {
    this.cycleColor = cycleColor
    this
  }

  /**
   * Convenience method for fluent usage.
   *
   * @param factorTransitive The factor with which actual node / edge colors are multiplied
   *                         when transitive.
   * @return
   */
  def setFactorTransitive(factorTransitive: Float): DotGraph = {
    this.factorTransitive = factorTransitive
    this
  }

  /**
   * Convenience method for fluent usage.
   *
   * @param doDetermineTransitiveEdges Determines whether to calculate transitive edges or not while calculating routes.
   * @return
   */
  def setDetermineTransitiveEdges(doDetermineTransitiveEdges: Boolean): DotGraph = {
    this.doDetermineTransitiveEdges = doDetermineTransitiveEdges
    this
  }

  /**
   * Calculates root nodes, leaf nodes, depth first search order of all nodes.
   *
   * @return T
   */
  override def initEntity(): Unit = {
    subGraphs.values.foreach(x => {
      x.initEntity()
    })
    determineCornerNodes()
    dfsGraph()
  }

  /**
   * Determines root and leaf nodes.
   */
  private[entity] def determineCornerNodes(): Unit = {
    nodes.values.foreach((node: DotNode) => {
      if (node.isRoot) roots.add(node)
      if (node.isLeaf) leafs.add(node)
    })
  }

  /**
   * Calculate dfs order for all nodes.
   */
  private[entity] def dfsGraph(): DotGraph = {
    val dfsSet = mutable.LinkedHashSet[DotNode]()
    if (roots.nonEmpty) roots.foreach(node => {
      clearVisited()
      dfsSet.addAll(node.dfs())
    }) else nodes.values.foreach(node => {
      clearVisited()
      dfsSet.addAll(node.dfs())
    })
    this.dfsSet.clear()
    this.dfsSet.addAll(dfsSet)
    clearVisited()
    this
  }

  /**
   * Calculates transitive edges and cycles.
   *
   * @return T
   */
  def determineGraphRoutes: DotGraph = {
    initEntity()
    subGraphs.values.foreach(_.determineGraphRoutes)

    // determine raw routes of all nodes
    dfsGraph()
    val reverse = dfsSet.toSeq.reverse
    reverse.foreach(node => {
      visited.clear()
      node.determineRoutes()
      this.routes.addAll(node.routes.filter(_.size > 1))
    })
    visited.clear()

    if (doDetermineTransitiveEdges) determineTransitiveEdges()

    determineCycles()

    nodes.values.foreach(_.initEntity())
    this
  }

  /**
   * Splits this graph in multiple subgraphs which will contain
   * nodes of a distinct category.
   *
   * @return DotGraph
   */
  def splitByCategory: DotGraph = {
    colorizeNodesFromPath()
    nodes.values.foreach(node => {
      removeNodeById(node.id, doRemoveEdges = false)
      val categoryName = node.category.getOrElse("")
      val _categoryGraph = getGraph("cluster-category-" + categoryName)
      var categoryGraph: DotGraph = null
      if (_categoryGraph.isDefined) categoryGraph = _categoryGraph.get
      else {
        categoryGraph = DotGraph("cluster-category-" + categoryName, graphType = GraphType.subgraph)
        categoryGraph.attributes.setLabel(categoryName).entity.legendType = CreateLegend.NONE
        addGraph(categoryGraph)
      }
      categoryGraph.addNode(node)
    })
    this
  }

  /**
   * Colorizes each node from the category of it's root node.
   */
  private def colorizeNodesFromPath(): Unit = {
    roots.foreach((root: DotNode) => {
      val networkNodes = determineNetwork(root)
      var categoryName = findCategory(networkNodes)
      // determine category and final color
      var color = root.getBgColor
      if (categoryName.isEmpty) categoryName = Some(root.id)
      color = addAndReturnCategory(categoryName.getOrElse(""))
      // finally set category and color for network nodes
      networkNodes.foreach(visitableNode => {
        visitableNode.category = categoryName
        visitableNode.setBgcolor(color)
      })
    })
    clearVisited()
  }

  /**
   * Finds out if any contained node has an explicitly set category color.
   *
   * @param nodes The reachable nodes.
   * @return
   */
  private def findCategory(nodes: Iterable[DotNode]): Option[String] = {
    if (nodes.isEmpty) Option.empty
    else {
      nodes.headOption.foreach(vn => if (vn.category.nonEmpty) return vn.category else Option.empty)
      findCategory(nodes.tail)
    }
  }

  /**
   * Returns the set of nodes which can be reached by this node and all nodes
   * which can reach the given node.
   *
   * @param node The node to check.
   * @return Set<DotNode>
   */
  def determineNetwork(node: DotNode): Set[DotNode] = {
    val nodes = mutable.Set[DotNode]()
    node.dfs().foreach((child: DotNode) => {
      nodes.add(child)
      clearVisited()
      val rootLine = child.rootLine()
      nodes.addAll(rootLine)
    })
    nodes.toSet
  }

  /**
   * Creates the legend for this graph. The legend contains at least the graphs name and the
   * creation date.
   */
  def createLegend: DotNode = {
    var legend: DotNode = null
    if (CreateLegend.NONE ne legendType) {
      val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm")
      val table = DotTable("LEGEND")
        .setTitle(DotRow().addCell(DotCell().setLabel(TextLabel("LEGEND")))).attributes
        .setCellspacing(0)
        .setBorder(2)
        .setCellborder(1)
        .entity
      legend = DotNode(this,"LEGEND").attributes
        .setShape(Shape.box)
        .setStyle(NodeStyle.filled)
        .setFillcolor(DotColor("#ffffff"))
        .setFontname("Verdana,Arial,Sans-Serif")
        .setLabel(table)
        .entity
      table.addRow(DotRow()
        .addCell(DotCell()
          .setLabel(TextLabel("<b>" + id + "</b>"))))
        .addRow(formatter.format(new Date(System.currentTimeMillis)))
      if (CreateLegend.FULL eq legendType) {
        val keys = mutable.ListBuffer[String]()
        keys.addAll(categories.keySet)
        keys.sortWith(_.toLowerCase() > _.toLowerCase())
        keys.foreach((key: String) => {
          val bgColor = categories(key)
          val fgColor = if (bgColor == DotColor.BLACK) DotColor.WHITE else DotColor.BLACK
          table.addRow(key, fgColor = fgColor, bgColor = bgColor)
        })
      }
    }
    legend
  }

  /**
   * Calculates transitive edges within this graph.
   * An edge is considered transitive when at least one other and longer route to the destination
   * node exists.
   */
  def determineTransitiveEdges(): Unit = {
    val routeTable = getRouteTable
    nodes.values.foreach(from => {
      routeTable.getRow(from).foreach(row => {
        row.foreach(entry => {
          val to: DotNode = entry._1
          val r: mutable.Seq[ListBuffer[DotNode]] = entry._2
          val directEdges: Set[DotEdge] = getEdges(from, to)
          val indirectRoutes = r.filter(n => n.size > 2 && n.last == to)
          if (indirectRoutes.nonEmpty && directEdges.nonEmpty) directEdges.foreach(edge => {
            edge.transitive = true
            var color = edge.attributes.getColor
            if (color == null) color = DotColor("#000000")
            color = color.multiply(DotColorComponent.SATURATION, factorTransitive)
            edge.attributes.setColor(color)
          })
        })
      })
    })
  }

  /**
   * Calculates cycles within this graph.
   */
  private def determineCycles(): Unit = {
    nodes.values.foreach((node: DotNode) => {
      node.processCyclicRoutes()
    })
  }

  /**
   * Returns all routes leading to the given node.
   *
   * @param to The destination node
   * @return Collection<DotRoute>
   */
  private[entity] def getRoutesTo(from: DotNode, to: DotNode): mutable.Set[mutable.ListBuffer[DotNode]] = {
    var routes = this.routes.filter(route => {
      val r = route.toSeq
      r.head == from && r.last == to
    })
    if(from.getEdges(to).nonEmpty) routes += mutable.ListBuffer[DotNode](to)
    routes
  }

  /**
   * Converts the given dot file to a document in the given output format.
   * The method first writes the dot file to <targetDirectory>/<outFileName>.dot
   * and then the final output file to <targetDirectory>/<outFileName>.<format>.
   *
   * @param dotExecutable The dot executable
   * @param targetDirectory The target directory to store the output files.
   * @param outFileName The output filename without extension.
   * @param format The output format.
   * @param dpi The output resolution in dots per Inch
   */
  def output(targetDirectory: File, outFileName: String = this.id, format: String = "pdf", dpi: Int = 72, dotExecutable: File = OsUtil.dotExecutable): DotGraph = {
    val isWindoze = OsUtil.isWindoze
    val dotFile = new File(targetDirectory, outFileName + ".dot")
    val outFile = new File(targetDirectory, outFileName + "." + format)
    attributes.setDpi(dpi)
    this.save(dotFile)
    val pdfDirectory = outFile.getParentFile
    pdfDirectory.mkdirs()
    val outFileArg = if(isWindoze) "-o \"" + outFile.getCanonicalPath + "\"" else "-o " + outFile.getCanonicalPath
    val dotFileArg = if(isWindoze) "\"" + dotFile.getCanonicalPath + "\"" else dotFile.getCanonicalPath
    val cmd = ShellCommand(dotExecutable, "-T" + format, outFileArg, dotFileArg)
    if (dotExecutable.exists) {
      println("[INFO] CMD: " + cmd.commandLine)
      println("[INFO] Writing pdf file to '" + outFile + "'")
      val stdout = cmd.execute
      if (stdout.nonEmpty) println(stdout)
    }
    else {
      println("[WARN] Skipping conversion of dot to PDF file because either your platform is not supported.")
      println("[WARN] or the dot executable does not exist.")
      println("[WARN] Try to update the tools/bin repository.")
      println("[WARN] You may also convert manually with the following command:")
      println("[WARN] > " + cmd.commandLine)
    }
    this
  }

  /**
   * Saves this graph to the given file. The graph must be initialized before.
   *
   * @param dotFile The target file.
   */
  def save(dotFile: File): DotGraph = {
    println("[INFO] Writing dot file to '" + dotFile + "'")
    dotFile.getParentFile.mkdirs()
    Files.write(dotFile.toPath, toString.getBytes())
    this
  }

  /**
   * Add the given node to this graph.
   *
   * @param id The unique id.
   * @return <T extends DotGraph>
   */
  def addNodeById(id: String, category: Option[String] = Option.empty): DotGraph = {
    addAndReturnNodeById(id, category)
    this
  }

  def addAndReturnNodeById(id: String, category: Option[String] = Option.empty): DotNode = {
    val _node = getNodeById(id)
    var node: DotNode = null
    if (_node.isDefined) node = _node.get
    else {
      node = DotNode(this, id, category)
      addNode(node)
    }
    node
  }

  /**
   * Add the given node to this graph.
   *
   * @param node The node.
   * @return <T extends DotGraph>
   */
  def addNode(node: DotNode): DotGraph = {
    addAndReturnNode(node)
    this
  }

  def addAndReturnNode(node: DotNode) : DotNode = {
    val id = node.id
    val _n = getNodeById(id)
    var n: DotNode = null
    if (_n.isDefined) n = _n.get
    else {
      n = node
      node.graph = this
      nodes.put(id, node)
      if (id.isEmpty) templateNodes.add(node)
    }
    n
  }

  /**
   * Remove the given node to this graph without destroying the edges.
   *
   * @param id The unique id.
   * @return <T extends DotGraph>
   */
  def removeNodeOnly(id: String): Option[DotNode] = removeNodeById(id, doRemoveEdges = false)

  /**
   * Remove the given node to this graph.
   *
   * @param id The unique id.
   * @return Option[DotNode]
   */
  def removeNodeById(id: String, doRemoveEdges: Boolean = true): Option[DotNode] = {
    val _node = nodes.remove(id)
    if (_node.isDefined) {
      val node = _node.get
      if (doRemoveEdges) node.edges.foreach(edge => removeEdges(edge.from, edge.to))
      roots.remove(node)
      leafs.remove(node)
    }
    _node
  }

  /**
   * Returns the list of known nodes in this specific graph excluding subgraphs.
   *
   * @return <T extends DotNode> Set<T>
   */
  def getNodes: Set[DotNode] = nodes.values.toSet

  /**
   * Returns the list of all known nodes within this graph including subgraphs.
   *
   * @return <T extends DotNode> Set<T>
   */
  def getAllNodes: Set[DotNode] = {
    val nodes = mutable.Set[DotNode]()
    nodes.addAll(this.nodes.values)
    subGraphs.values.foreach((graph: DotGraph) => {
      nodes.addAll(graph.nodes.values)
    })
    nodes.toSet
  }

  /**
   * Returns whether the given node exists in this graph excluding subgraphs.
   *
   * @param id The unique id.
   * @return boolean
   */
  def containsNodeById(id: String): Boolean = nodes.contains(id)

  /**
   * Returns whether the given node exists in this graph excluding subgraphs.
   *
   * @param node The node.
   * @return boolean
   */
  def containsNode(node: DotNode): Boolean = nodes.values.toSeq.contains(node)

  /**
   * Retrieves the given node from this graph and its subgraphs.
   *
   * @param id The unique id.
   * @return <T extends DotNode>
   */
  def getNodeById(id: String): Option[DotNode] = {
    var node = nodes.get(id)
    if (node.isEmpty) {
      node = findNode(subGraphs.values, id)
    }
    node
  }

  private def findNode(iter: Iterable[DotGraph], id: String): Option[DotNode] = {
    if (iter.isEmpty) Option.empty
    else {
      iter.headOption.foreach(subGraph => {
        val node = subGraph.getNodeById(id)
        if (node.isDefined) return node
        else Option.empty
      })
      findNode(iter.tail, id)
    }
  }

  /**
   * Creates the node with the given id if it does not exist in this graph
   * or returns the existing node.
   *
   * @param id The node id to retrieve
   * @return <T extends DotNode>
   */
  def createNodeById(id: String): DotNode = {
    val _node = getNodeById(id)
    if (_node.isDefined) _node.get
    else  addAndReturnNodeById(id)
  }

  def removeEdgesById(fromId: String, toId: String): Unit = {
    val from = getNodeById(fromId)
    val to = getNodeById(toId)
    if (from.isDefined && to.isDefined) removeEdges(from.get, to.get)
  }

  def removeEdges(from: DotNode, to: DotNode): Unit = {
   getEdges(from, to).foreach(edge => {
      from.removeEdges(edge.to)
      to.removeReferences(edge.from)
    })
  }

  def removeAllEdgesById(fromId: String, toId: String): Unit = {
    val from = getNodeById(fromId)
    val to = getNodeById(toId)
    if (from.isDefined && to.isDefined) removeAllEdges(from.get, to.get)
  }

  def removeAllEdges(from: DotNode, to: DotNode): Unit = {
    getAllEdges(from, to).foreach(edge => {
      from.removeEdges(edge.to)
      to.removeReferences(edge.from)
    })
  }

  def addEdgeById(fromId: String, toId: String, label: String = "", color: DotColor = DotColor.BLACK, arrowHead: Option[ArrowType] = None): DotGraph = {
    val edge: Option[DotEdge] = addAndReturnEdgeById(fromId, toId, label)
    edge.map(_.attributes.setColor(color))
    arrowHead.foreach(ah => edge.foreach(_.attributes.setArrowhead(ah)))
    this
  }

  def addAndReturnEdgeById(fromId: String, toId: String, label: String = ""): Option[DotEdge] = {
    val from = getNodeById(fromId)
    val to = getNodeById(toId)
    if (from.isDefined && to.isDefined) addAndReturnEdge(from.get, to.get, label)
    else Option.empty
  }

  def addEdge(from: DotNode, to: DotNode, label: String = ""): DotGraph = {
    addAndReturnEdge(from, to, label)
    this
  }

  def addAndReturnEdge(from: DotNode, to: DotNode, label: String = ""): Option[DotEdge] = {
    var edge = getEdge(from, to, label)
    if (edge.isEmpty && containsNode(from) && containsNode(to)) edge = {
      to.addReference(from, label)
      Some(from.addEdge(to, label))
    }
    edge
  }

  def removeTransitiveEdges(): Unit = {
    getGraphEdges.filter(_.transitive).foreach(edge => removeEdges(edge.from, edge.to))
  }

  /**
   * Returns the edges of this graph excluding its sub graphs..
   *
   * @return Set<T>
   */
  def getGraphEdges: Set[DotEdge] = {
    nodes.values.flatMap(_.edges).toSet
  }

  /**
   * Returns the edges of this graph excluding its sub graphs
   * which point from and to the given nodes..
   *
   * @return Set<T>
   */
  def getEdgesById(fromId: String, toId: String): Set[DotEdge] = {
    val from = getNodeById(fromId)
    val to = getNodeById(toId)
    if (from.isDefined && to.isDefined) getEdges(from.get, to.get)
    else Set[DotEdge]()
  }

  def getEdgeById(fromId: String, toId: String, label: String): Option[DotEdge] = {
    getEdgesById(fromId, toId).find(_.label == label)
  }

  def getEdge(from: DotNode, to: DotNode, label: String): Option[DotEdge] = {
    getEdges(from, to).find(_.label == label)
  }

  def getEdges(from: DotNode, to: DotNode): Set[DotEdge] = {
    getGraphEdges.filter(it => from == it.from && to == it.to)
  }

  /**
   * Returns the edges of this graph including its sub graphs
   * which point from and to the given nodes..
   *
   * @return Set<T>
   */
  def getAllEdgesById(fromId: String, toId: String): Set[DotEdge] = {
    val from = getNodeById(fromId)
    val to = getNodeById(toId)
    if (from.isDefined && to.isDefined) getAllEdges(from.get, to.get)
    else Set[DotEdge]()
  }

  def getAllGraphEdges: Set[DotEdge] = {
    val g1 = getGraphEdges
    val g2 = subGraphs.values.flatMap(_.getGraphEdges)
    val g3 = g1 ++ g2
    g3
  }

  def getAllEdges(from: DotNode, to: DotNode): Set[DotEdge] = {
      getAllGraphEdges.filter(it => it.from == from && it.to == to)
  }

  def containsEdgeById(fromId: String, toId: String): Boolean = {
    val from = getNodeById(fromId)
    val to = getNodeById(toId)
    if (from.isDefined && to.isDefined) containsEdge(from.get, to.get)
    else false
  }

  def containsEdge(from: DotNode, to: DotNode): Boolean = getEdges(from, to).nonEmpty

  /**
   * Returns whether the given sub graph is contained in this graph or not.
   *
   * @param id The id to retrieve.
   * @return boolean
   */
  def containsGraph(id: String): Boolean = subGraphs.contains(id)

  /**
   * Returns the given sub graph (if any) or null.
   *
   * @param id The id to retrieve.
   * @return <T extends DotGraph>
   */
  def getGraph(id: String): Option[DotGraph] = {
    var graph = subGraphs.get(id)
    if (graph.isEmpty && subGraphs.nonEmpty) {
      graph = findGraph(subGraphs.values, id)
    }
    graph
  }

  private def findGraph(iter: Iterable[DotGraph], id: String): Option[DotGraph] = {
    if (iter.isEmpty) Option.empty
    else {
      iter.headOption.foreach(subgraph => {
        val graph = subgraph.getGraph(id)
        if (graph.isDefined) return graph
        else Option.empty
      })
      findGraph(iter.tail, id)
    }
  }

  /**
   * Add the given subgraph to this graph.
   *
   * @param graph The graph.
   * @return <T extends DotGraph>
   */
  def addGraph(graph: DotGraph): DotGraph = {
    assert(graph.graphType eq GraphType.subgraph, "Subgraphs must be of type subgraph")
    assert(graph.id.startsWith("cluster"), "Subgraph ids must start with 'cluster'")
    graph.parentGraph = this
    subGraphs.put(graph.id, graph)
    this
  }

  /**
   * Remove the given subgraph from this graph.
   *
   * @param graph The graph.
   *
   */
  def removeGraph(graph: DotGraph): Unit = {
    subGraphs.remove(graph.id)
  }

  /**
   * Returns a from - to mapping of all known routes whithin this graph.
   *
   * @return TreeMultitable<DotNode, DotNode, DotRoute>
   */
  def getRouteTable: MultiListBufferTable[DotNode, DotNode, mutable.ListBuffer[DotNode]] = {
    val routeTable = MultiListBufferTable[DotNode, DotNode, mutable.ListBuffer[DotNode]]()
    routes.foreach((route: mutable.ListBuffer[DotNode]) => {
      val routeList = route.toList
      val from = routeList.apply(0)
      val to = routeList.apply(route.size - 1)
      routeTable.addOneValue(from, to, route)
    })
    routeTable
  }

  /**
   * Clears the visited flag of all nodes.
   */
  def clearVisited(): Unit = {
    visited.clear()
  }

  def getRootGraph: DotGraph = {
    var rootGraph: DotGraph = this
    while (rootGraph.parentGraph != null) rootGraph = rootGraph.parentGraph
    rootGraph
  }

  /**
   * Adds a new category to this graph.
   *
   * @param categoryName The category to add.
   * @return DotGraph
   */
  def addCategory(categoryName: String, color: Option[DotColor] = Option.empty): DotGraph = {
    addAndReturnCategory(categoryName, color)
    this
  }

  /**
   * Adds a new category to this graph.
   *
   * @param categoryName The category to add.
   * @return String
   */
  def addAndReturnCategory(categoryName: String, color: Option[DotColor] = Option.empty): DotColor = {
    categories.getOrElseUpdate(categoryName, color.getOrElse(DistinctColorGoldenAngle.getNextColor()))
  }

  /**
   * Removes the given category from the categories of this graph.
   *
   * @param categoryName The category name.
   */
  def removeCategory(categoryName: String): Unit = {
    categories.remove(categoryName)
  }

  def empty: Boolean = subGraphs.isEmpty && nodes.isEmpty

  def size: Int = nodes.size

  override def compare(that: DotGraph): Int = id.compare(that.id)

  def canEqual(other: Any): Boolean = other.isInstanceOf[DotGraph]

  override def equals(other: Any): Boolean = other match {
    case that: DotGraph =>
      (that canEqual this) &&
        id == that.id
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(id)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

object DotGraph {

  def apply(id: String, strict: Boolean = false, graphType: GraphType = GraphType.digraph) = new DotGraph(id, strict, graphType)

  val SIZE_A4: Point = Point(7.5, 10.0)
  val RATIO_AUTO: Point = Point(-1, -1)
  val RATIO_COMPRESS: Point = Point(-2, -2)
  val RATIO_FILL: Point = Point(-3, -3)
}
