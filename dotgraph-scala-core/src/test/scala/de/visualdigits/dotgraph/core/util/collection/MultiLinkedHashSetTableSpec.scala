package de.visualdigits.dotgraph.core.util.collection

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

class MultiLinkedHashSetTableSpec extends FlatSpec with Matchers {

  "A MultiLinkedHashSetTableSpec" should "behave as expected" in {
    val table = MultiLinkedHashSetTable[String, String, Int]()

    table.addOneValue("B", "A", 3) should be(Option.empty)
    table.addOneValue("B", "B", 4) should be(Option.empty)
    table.addOneValue("B", "B", 4) should be(Some(4))
    table.addOneValue("A", "A", 1) should be(Option.empty)
    table.addOneValue("A", "B", 2) should be(Option.empty)
    table.get("B", "B") should be(Some(mutable.LinkedHashSet(4)))

    table.addOneValue("B", "B", 5) should be(Some(4))
    table.get("B", "B") should be(Some(mutable.LinkedHashSet(4, 5)))

    table.size() should be(5)

    table.toString should be("LinkedHashMap(B -> LinkedHashMap(A -> LinkedHashSet(3), B -> LinkedHashSet(4, 5)), A -> LinkedHashMap(A -> LinkedHashSet(1), B -> LinkedHashSet(2)))")
  }
}
