package de.visualdigits.dotgraph.core.entity

import java.io.File

import de.visualdigits.dotgraph.core.`type`._
import org.junit.Assert._
import org.junit.Test

import scala.collection.mutable

class DotGraphTest {

  val dotExecutable = new File("/usr/local/bin/dot")
  val targetDirectory: File = new File("/Users/knull/dotgraph/")

  @Test def testCRUD(): Unit = {
    val subgraph = DotGraph("cluster", graphType = GraphType.subgraph)
      .setCreateLegend(CreateLegend.NONE)
      .setDetermineTransitiveEdges(true)
      .addNodeById("ca")
      .addNodeById("cb")
      .addNodeById("cc")
      .addNodeById("cd")
      .addEdgeById("cc", "ca", "foo")
      .addEdgeById("cc", "cb", "bar")
      .addEdgeById("cc", "cd", "baz")

    val graph = DotGraph("graph")
          .setDetermineTransitiveEdges(true)
          .addNodeById("a")
          .addNodeById("b")
          .addNodeById("c")
          .addNodeById("orphan")
          .addEdgeById("b", "a")
          .addEdgeById("c", "a")
          .addGraph(subgraph)
          .output(targetDirectory, "graph", dotExecutable = dotExecutable)

    val edges = subgraph.getGraphEdges
    assertEquals("Number of edges in subgraph not as expected", 3, edges.size)
    assertEquals("Number of edges in graph not as expected", 2, graph.getGraphEdges.size)
    assertEquals("Number of edges in subgraph not as expected", 3, subgraph.getGraphEdges.size)
    assertEquals("Number of edges in graph not as expected", 5, graph.getAllGraphEdges.size)
    assertEquals("Number of edges in subgraph not as expected", 4, subgraph.getNodes.size)
    assertEquals("Number of edges in graph not as expected", 4, graph.getNodes.size)
    assertEquals("Number of edges in subgraph not as expected", 4, subgraph.getAllNodes.size)
    assertEquals("Number of edges in graph not as expected", 8, graph.getAllNodes.size)
    assertTrue("ListNode not found", graph.containsNodeById("a"))
    assertFalse("ListNode found", graph.containsNodeById("foo"))
    assertFalse("ListNode found", subgraph.containsNodeById("a"))
    assertTrue("Edge not found", graph.containsEdgeById("b", "a"))
    assertFalse("Edge found", graph.containsEdgeById("a", "b"))

    val a = graph.getNodeById("a")
    val b = graph.getNodeById("b")
    val c = graph.getNodeById("c")
    val orphan = graph.getNodeById("orphan")
    val a1 = graph.addAndReturnNodeById("a")
    assertEquals("Twice added nodes are not equal", a.get, a1)
    val eba = graph.getEdgesById("b", "a")
    assertEquals("Unexpected edge size", eba.size, 1)
    val eba1 = graph.addAndReturnEdgeById("b", "a")
    assertEquals("Twice added edges are not equal", eba.iterator.next, eba1.get)
    val eba2 = graph.addAndReturnEdgeById("b", "a", "boo")
    assertNotEquals("Edges with different labels considered equal", eba1.get, eba2.get)
    assertFalse("Graph considered as strict", graph.isStrict)
    assertFalse("ListNode a is considered as orphan", a.get.isOrphan)
    assertFalse("ListNode b is considered as orphan", b.get.isOrphan)
    assertFalse("ListNode c is considered as orphan", c.get.isOrphan)
    assertTrue("ListNode orphan is considered as orphan", orphan.get.isOrphan)
    assertFalse("Node a has edges", c.get.edges.isEmpty)
    assertTrue("Node a has edges", a.get.edges.isEmpty)
    assertFalse("Node a has references", a.get.references.isEmpty)
    assertTrue("Node a has references", c.get.references.isEmpty)
    val cba  = graph.getEdges(b.get, a.get)
    assertFalse("Edge not found", cba.isEmpty)
    val ba= cba.iterator.next
    assertNotNull("Node from iterator was null", ba)
    assertTrue("Edge found", graph.getEdges(a.get, b.get).isEmpty)
    assertFalse("Edge found", graph.getEdges(b.get, a.get).isEmpty)
    assertTrue("Edge found", graph.getEdges(a.get, orphan.get).isEmpty)
    assertTrue("Edge not found", graph.getEdgesById("cc", "ca").isEmpty)
    assertFalse("Edge not found", subgraph.getEdgesById("cc", "ca").isEmpty)
    assertTrue("Edge not found", subgraph.getEdgesById("a", "b").isEmpty)
    assertFalse("Edge not found", graph.getEdgesById("b", "a").isEmpty)
    assertTrue("Edge found", graph.getEdgesById("a", "b").isEmpty)
    assertTrue("Edge found in graph which should be only in subgraph", graph.getEdgesById("cc", "ca").isEmpty)
    assertFalse("Edge not found in subgraph", subgraph.getEdgesById("cc", "ca").isEmpty)
    assertFalse("Subgraph edge not found in graph using getAllEdge()", graph.getAllEdgesById("cc", "ca").isEmpty)
    assertEquals("Unexpected number of nodes for dfs", 3, graph.determineGraphRoutes.dfsSet.size)
    assertEquals("Unexpected number of nodes", 4, graph.getNodes.size)
    assertEquals("Unexpected number of root nodes", 1, graph.roots.size)
    assertEquals("Unexpected number of leaf nodes", 2, graph.leafs.size)
    assertEquals("Unexpected number of edges", 3, graph.getGraphEdges.size)
    assertEquals("Unexpected number of edges", 0, a.get.edges.size)
    assertEquals("Unexpected number of references", 3, a.get.references.size)
    assertEquals("Unexpected number of edges", 2, b.get.edges.size)
    assertFalse("ListNode found in subgraph", subgraph.containsNode(a.get))
    //        assertFalse("Edge found in subgraph", subgraph.containsEdgeById(ba));
    assertTrue("Edge not found in subgraph", subgraph.containsEdgeById("cc", "ca"))
    assertFalse("Edge found in graph", graph.containsEdgeById("cc", "ca"))
    assertTrue("Edge not found in node", graph.getNodeById("cc").get.containsEdge(graph.getNodeById("ca").get))
    assertTrue("Edge not found in node", graph.getNodeById("ca").get.containsReference(graph.getNodeById("cc").get))
    assertFalse("Edge not found in node", graph.getNodeById("cc").get.containsEdge(graph.getNodeById("c").get))
    assertFalse("Edge not found in node", graph.getNodeById("c").get.containsReference(graph.getNodeById("cc").get))
    graph.removeNodeById("a")
    assertFalse("Graph contains node 'a' after removal", graph.containsNode(a.get))
    assertTrue("Edge cc -> ca not found in subgraph", subgraph.containsEdgeById("cc", "ca"))
    assertFalse("Edge cc -> ca found in graph", graph.containsEdgeById("cc", "ca"))
    graph.removeEdgesById("cc", "ca")
    assertTrue("Edge cc -> ca not found in subgraph after removal in parent graph which should not work", subgraph.containsEdgeById("cc", "ca"))
    subgraph.removeEdgesById("cc", "ca")
    assertFalse("Edge cc -> ca not in subgraph after removal", subgraph.containsEdgeById("cc", "ca"))
    assertTrue("Edge cc -> cd not found in subgraph before removal", subgraph.containsEdgeById("cc", "cd"))
    subgraph.removeNodeById("cc")
    assertFalse("Edge cc -> cd found in subgraph after removal of node 'cc'", subgraph.containsEdgeById("cc", "cd"))
    assertFalse("ListNode 'foo' found in graph before adding - woodoo?", graph.containsNodeById("foo"))
    val node: DotNode = graph.createNodeById("foo")
    assertNotNull("No node returned from createNode", node)
    assertTrue("ListNode 'foo' not found in graph after adding", graph.containsNodeById("foo"))
    val node2: DotNode = graph.createNodeById("foo")
    assertNotNull("No node returned from createNode", node2)
    assertEquals("Created node 'foo' twice", node, node2)
  }

  @Test def testSimple(): Unit = {
    DotGraph("simple")
          .setDetermineTransitiveEdges(true)
          .addNodeById("a")
          .addNodeById("b")
          .addNodeById("c")
          .addNodeById("orphan")
          .addEdgeById("b", "a")
          .addEdgeById("c", "a")
          .output(targetDirectory, "simple", dotExecutable = dotExecutable)
  }

  @Test def testFindSubGraph(): Unit = {
    val graph = DotGraph("graph")
      .addGraph(DotGraph("cluster-sub", graphType = GraphType.subgraph)
        .addGraph(DotGraph("cluster-sub-sub1", graphType = GraphType.subgraph))
        .addGraph(DotGraph("cluster-sub-sub2", graphType = GraphType.subgraph))
        .addGraph(DotGraph("cluster-sub-sub3", graphType = GraphType.subgraph))
      )

    var foundGraph = graph.getGraph("Foo")
    assertEquals("Found not existing sub graph", Option.empty, foundGraph)
    foundGraph = graph.getGraph("cluster-sub-sub2")
    assertNotEquals("Not found existing sub graph", Option.empty, foundGraph)

  }

  @Test def testSelfCycle(): Unit = {
    val graph = DotGraph("graph")
          .setDetermineTransitiveEdges(true)
          .addNodeById("a")
          .addNodeById("b")
          .addNodeById("c")
          .addNodeById("d")
          .addNodeById("e")
          .addEdgeById("c", "d")
          .addEdgeById("b", "a")
          .addEdgeById("d", "a")
          .addEdgeById("a", "e")
          .addEdgeById("e", "e")
          .determineGraphRoutes
          .output(targetDirectory, "self-cycle", dotExecutable = dotExecutable)
    val b = graph.getNodeById("b")
    val d = graph.getNodeById("d")
    val e = graph.getNodeById("e")
    assertFalse("ListNode 'b' considered to be part of cycle", b.get.partOfCycle)
    assertFalse("ListNode 'd' considered to be part of cycle", d.get.partOfCycle)
    assertTrue("ListNode 'e' not considered to be part of cycle", e.get.partOfCycle)
  }

  @Test def testCycle(): Unit = {
    val graph = DotGraph("graph")
          .setCycleColors(DotColor("#ff7700"))
          .setFactorTransitive(0.2)
          .setDetermineTransitiveEdges(true)
          .addNodeById("a")
          .addNodeById("b")
          .addNodeById("c")
          .addNodeById("d")
          .addNodeById("e")
          .addEdgeById("c", "d")
          .addEdgeById("b", "a")
          .addEdgeById("d", "a")
          .addEdgeById("a", "e")
          .addEdgeById("e", "b")
          .determineGraphRoutes
          .output(targetDirectory, "cycle", dotExecutable = dotExecutable)
    val b = graph.getNodeById("b")
    val d = graph.getNodeById("d")
    val e = graph.getNodeById("e")
    assertTrue("ListNode 'b' not considered to be part of cycle", b.get.partOfCycle)
    assertFalse("ListNode 'd' considered to be part of cycle", d.get.partOfCycle)
    assertTrue("ListNode 'e' not considered to be part of cycle", e.get.partOfCycle)
  }

  @Test def testCluster1(): Unit = {
    val graph = DotGraph("cluster1")
    graph.setDetermineTransitiveEdges(true)
          .attributes.setStyle(ClusterStyle.dotted)
          .setColor(DotColor("blue"))
          .setFillcolor(DotColor("#cccccc"))
          .entity
          .addNode(DotNode(graph, "a1")
            .attributes.setStyle(NodeStyle.filled)
            .setFillcolor(DotColor("red"))
            .entity)
          .addNodeById("b1")
          .addNodeById("c1")
          .addNodeById("d1")
          .addEdgeById("b1", "a1")
          .addEdgeById("c1", "a1")
          .addEdgeById("d1", "b1")
          .addEdgeById("d1", "c1")
          .addEdgeById("d1", "a1")
          .determineGraphRoutes
          .output(targetDirectory, "cluster1", dotExecutable = dotExecutable)
  }

  @Test def testCluster2(): Unit = {
    val graph = DotGraph("cluster2", false, GraphType.digraph)
    graph.setDetermineTransitiveEdges(true)
          .addNode(DotNode(graph, "a2")
            .attributes
            .setStyle(NodeStyle.filled).setFillcolor(DotColor("red"))
            .entity)
          .addNodeById("b2")
          .addNodeById("c2")
          .addNodeById("d2")
          .addNodeById("e2")
          .addEdgeById("b2", "a2")
          .addEdgeById("c2", "a2")
          .addEdgeById("c2", "d2")
          .addEdgeById("d2", "e2")
          .determineGraphRoutes
          .output(targetDirectory, "cluster2", dotExecutable = dotExecutable)
  }

  @Test def testNesting(): Unit = {
    val cluster1 = DotGraph("cluster1", graphType = GraphType.subgraph)
      .setCreateLegend(CreateLegend.NONE)
      .setDetermineTransitiveEdges(true)
      .addNodeById("a1")
      .addNodeById("b1")
      .addNodeById("c1")
      .addEdgeById("b1", "a1")
      .addEdgeById("c1", "a1")

    val cluster2 = DotGraph("cluster2", graphType = GraphType.subgraph)
      .setCreateLegend(CreateLegend.NONE)
      .setDetermineTransitiveEdges(true)
      .addNodeById("a2")
      .addNodeById("b2")
      .addNodeById("c2")
      .addEdgeById("b2", "a2")
      .addEdgeById("c2", "a2")
      .addGraph(cluster1)

    val graph = DotGraph("graph")
    graph.setDetermineTransitiveEdges(true)
          .addNode(DotNode(graph)
            .attributes.setShape(Shape.rect)
            .setStyle(NodeStyle.filled)
            .setFillcolor(DotColor("lightblue"))
            .entity)
          .addNodeById("a")
          .addNodeById("b")
          .addNodeById("c")
          .addEdgeById("b", "a")
          .addEdgeById("c", "a")
          .addGraph(cluster2)
          .output(targetDirectory, "nested", dotExecutable = dotExecutable)
  }

  @Test def testSubGraphSingle(): Unit = {
    val graph = DotGraph("subgraph single", false, GraphType.digraph)
    graph.setDetermineTransitiveEdges(true)
          .addNode(DotNode(graph, "a1")
            .attributes
            .setStyle(NodeStyle.filled)
            .setFillcolor(DotColor("red"))
            .entity)
          .addNodeById("b1")
        .addNodeById("c1")
          .addNodeById("d1")
          .addNode(DotNode(graph,"a2")
            .attributes
            .setStyle(NodeStyle.filled)
            .setFillcolor(DotColor("red"))
            .entity)
          .addNodeById("b2")
          .addNodeById("c2")
          .addNodeById("d2")
          .addNodeById("e2")
          .addNodeById("A")
          .addNodeById("Z")
          .addEdgeById("b1", "a1")
          .addEdgeById("c1", "a1")
          .addEdgeById("d1", "b1")
          .addEdgeById("d1", "c1")
          .addEdgeById("d1", "a1")
          .addEdgeById("b2", "a2")
          .addEdgeById("c2", "a2")
          .addEdgeById("c2", "d2")
          .addEdgeById("d2", "e2")
          .addEdgeById("Z", "c2")
          .addEdgeById("Z", "c1")
          .addEdgeById("a1", "A")
          .addEdgeById("e2", "A")
          .determineGraphRoutes
          .output(targetDirectory, "single-graph", dotExecutable = dotExecutable)
  }

  @Test def testSubGraph(): Unit = {
    val cluster1 = DotGraph("cluster1", graphType = GraphType.subgraph)
    cluster1.setCreateLegend(CreateLegend.NONE)
      .setDetermineTransitiveEdges(true)
      .attributes
      .setLabel("Cluster 1")
      .setStyle(ClusterStyle.dotted)
      .setColor(DotColor("blue"))
      .setStyle(ClusterStyle.filled)
      .setFillcolor(DotColor("yellow"))
      .entity
      .addNode(DotNode(cluster1)
        .attributes
        .setStyle(NodeStyle.filled)
        .setFillcolor(DotColor("white"))
        .entity)
      .addNode(DotNode(cluster1, "a1")
        .attributes
        .setStyle(NodeStyle.filled)
        .setFillcolor(DotColor("red"))
        .entity)
      .addNodeById("b1")
      .addNodeById("c1")
      .addNodeById("d1")
      .addEdgeById("b1", "a1")
      .addEdgeById("c1", "a1")
      .addEdgeById("d1", "b1")
      .addEdgeById("d1", "c1")
      .addEdgeById("d1", "a1")

    val cluster2 = DotGraph("cluster2", graphType = GraphType.subgraph)
    cluster2.setCreateLegend(CreateLegend.NONE)
      .setDetermineTransitiveEdges(true)
      .attributes
      .setLabel("Cluster 2")
      .entity
      .addNode(DotNode(cluster2)
        .attributes
        .setStyle(NodeStyle.filled)
        .setFillcolor(DotColor("green"))
        .entity).addNode(DotNode(cluster2, "a2")
      .attributes
      .setStyle(NodeStyle.filled)
      .setFillcolor(DotColor("red"))
      .entity)
      .addNodeById("b2")
      .addNodeById("c2")
      .addNodeById("d2")
      .addNodeById("e2")
      .addEdgeById("b2", "a2")
      .addEdgeById("c2", "a2")
      .addEdgeById("c2", "d2")
      .addEdgeById("d2", "e2")

    val graph = DotGraph("graph")
    graph.setDetermineTransitiveEdges(true)
          .addNode(DotNode(graph)
            .attributes
            .setStyle(NodeStyle.filled)
            .setFillcolor(DotColor("lightblue"))
            .entity)
          .addNodeById("A")
          .addGraph(cluster1)
          .addGraph(cluster2)
          .addNodeById("Z")
          .addEdgeById("Z", "c2")
          .addEdgeById("Z", "d1")
          .addEdgeById("a1", "A")
          .addEdgeById("e2", "A")
          .determineGraphRoutes
          .output(targetDirectory, "subgraph", dotExecutable = dotExecutable)
  }

  @Test def testMultipleGroups(): Unit = {
    val graph = DotGraph("test-graph", false, GraphType.digraph)
    graph.setCreateLegend(CreateLegend.FULL)
          .setDetermineTransitiveEdges(true)
          .addCategory("test-1", Some(DotColor("#ff0000")))
          .addNode(DotNode(graph)
            .attributes
            .setShape(Shape.rect)
            .setStyle(NodeStyle.filled)
            .entity)
          .addNodeById("a")
          .addNodeById("b", Some("test-1"))
          .addNodeById("c")
          .addNodeById("d")
          .addNodeById("e")
          .addNodeById("f")
          .addNodeById("g")
          .addNodeById("h")
          .addNodeById("i")
          .addNodeById("j")
          .addNodeById("k")
          .addNodeById("l")
          .addEdgeById("b", "a")
          .addEdgeById("b", "a")
          .addEdgeById("b", "a")
          .addEdgeById("b", "a")
          .addEdgeById("b", "c")
          .addEdgeById("b", "d")
          .addEdgeById("g", "e")
          .addEdgeById("g", "f")
          .addEdgeById("h", "f")
          .addEdgeById("i", "b")
          .addEdgeById("j", "g")
          .addEdgeById("k", "l")
          .determineGraphRoutes
          .splitByCategory
          .output(targetDirectory, "multiple-groups", dotExecutable = dotExecutable)
  }

  @Test def testBackLink(): Unit = {
    DotGraph("backlink-graph", false, GraphType.digraph)
          .setDetermineTransitiveEdges(true)
          .addNodeById("a")
          .addNodeById("b")
          .addNodeById("c")
          .addNodeById("d")
          .addEdgeById("b", "a")
          .addEdgeById("c", "a")
          .addEdgeById("a", "d")
          .addEdgeById("a", "b")
          .addEdgeById("a", "a")
          .determineGraphRoutes
          .output(targetDirectory, "backlink", dotExecutable = dotExecutable)
  }

  @Test //    @Ignore
  def testWarehouse(): Unit = {
    DotGraph("warehouse-graph", false, GraphType.digraph)
          .setDetermineTransitiveEdges(true)
          .addNodeById("a")
          .addNodeById("b")
          .addNodeById("c")
          .addNodeById("d")
          .addNodeById("e")
          .addNodeById("f")
          .addNodeById("g")
          .addNodeById("h")
          .addNodeById("i")
          .addNodeById("j")
          .addNodeById("k")
          .addNodeById("l")
          .addNodeById("m")
          .addNodeById("n")
          .addNodeById("o")
          .addNodeById("p")
          .addNodeById("q")
          .addNodeById("r")
          .addNodeById("s")
          .addNodeById("t")
          .addEdgeById("r", "s")
          .addEdgeById("r", "n")
          .addEdgeById("n", "s")
          .addEdgeById("n", "i")
          .addEdgeById("c", "s")
          .addEdgeById("c", "k")
          .addEdgeById("c", "c")
          .addEdgeById("l", "g")
          .addEdgeById("o", "h")
          .addEdgeById("b", "m")
          .addEdgeById("q", "h")
          .addEdgeById("g", "l")
          .addEdgeById("g", "a")
          .addEdgeById("m", "d")
          .addEdgeById("m", "h")
          .addEdgeById("f", "l")
          .addEdgeById("f", "m")
          .addEdgeById("f", "i")
          .addEdgeById("a", "g")
          .addEdgeById("j", "i")
          .addEdgeById("p", "m")
          .addEdgeById("e", "m")
          .addEdgeById("k", "c")
          .addEdgeById("d", "h")
          .addEdgeById("i", "f")
          .addEdgeById("i", "j")
          .addEdgeById("h", "d")
          .addEdgeById("h", "q")
          .addEdgeById("h", "h")
          .addEdgeById("h", "o")
          .addEdgeById("h", "a")
          .addEdgeById("s", "f")
          .addEdgeById("s", "r")
          .addEdgeById("s", "c")
          .addEdgeById("t", "h")
    //      .determineGraphRoutes
          .output(targetDirectory, "warehouse", dotExecutable = dotExecutable)
  }

  @Test def testRoutes(): Unit = {
    val graph = DotGraph("test-graph", false, GraphType.digraph)
    graph.setDetermineTransitiveEdges(true)
          .addNode(DotNode(graph)
            .attributes
            .setShape(Shape.rect)
            .setStyle(NodeStyle.filled)
            .setFillcolor(DotColor("yellow"))
            .entity)
          .addNodeById("a")
          .addNodeById("b")
          .addNode(DotNode(graph,"c")
            .attributes
            .setFillcolor(DotColor("red")).entity)
          .addNodeById("d")
          .addNodeById("e")
          .addNodeById("f")
          .addNodeById("g")
          .addNodeById("h")
          .addNodeById("x")
          .addNodeById("y")
          .addNodeById("z")
          .addNodeById("r")
          .addEdgeById("b", "a")
          .addEdgeById("c", "a")
          .addEdgeById("d", "c")
          .addEdgeById("e", "c")
          .addEdgeById("e", "a")
          .addEdgeById("e", "d")
          .addEdgeById("x", "a")
          .addEdgeById("y", "b")
          .addEdgeById("z", "e")
          .addEdgeById("z", "g")
          .addEdgeById("x", "r")
          .addEdgeById("d", "y")
          .addEdgeById("f", "x")
          .addEdgeById("g", "x")
          .addEdgeById("h", "f")
          .addEdgeById("h", "x")
          .addEdgeById("h", "g")
          .addEdgeById("e", "h")
          .addEdgeById("z", "y")
          .determineGraphRoutes
          .output(targetDirectory, "routes", dotExecutable = dotExecutable)
  }

  private def showGraph(graph: DotGraph) = {
    val sb = new StringBuilder()
    val routeTable = graph.getRouteTable
    routeTable.foreach(entry => {
      val from = entry._1
      entry._2.foreach(row => {
        val to = row._1
        val routes: mutable.ListBuffer[mutable.ListBuffer[DotNode]] = row._2
        sb ++= from.id + " -> " + to.id + ": " + routes.map("[" + _.map(_.id).mkString(", ") + "]").mkString(", ") + "\n"
      })
    })
    sb.toString
  }

}
