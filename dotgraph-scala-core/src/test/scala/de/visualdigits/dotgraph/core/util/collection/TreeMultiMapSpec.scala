package de.visualdigits.dotgraph.core.util.collection

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

class TreeMultiMapSpec extends FlatSpec with Matchers {

  "A MultiTreeSetTableSpec" should "behave as expected" in {
    val map = TreeMultiMap[String, Int]()

    map.addOne("B", 3) should be(Option.empty)
    map.addOne("B", 4) should be(Some(mutable.Buffer(3)))
    map.addOne("B", 4) should be(Some(mutable.Buffer(3, 4)))
    map.addOne("A", 1) should be(Option.empty)
    map.addOne("A", 2) should be(Some(mutable.Buffer(1)))
    map.get("B") should be(Some(mutable.Buffer(3, 4, 4)))

    map.addOne("B", 5) should be(Some(mutable.Buffer(3, 4, 4)))
    map.get("B") should be(Some(mutable.Buffer(3, 4, 4, 5)))

    map.size should be(6)

    map.toString should be("TreeMap(A -> ListBuffer(1, 2), B -> ListBuffer(3, 4, 4, 5))")
  }

}
