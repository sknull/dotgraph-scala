package de.visualdigits.dotgraph.core.util.collection

import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TableSpec extends AnyFlatSpec with Matchers {

  "A Table" should "behave as expected" in {
    val table = Table[String, String, Int]()
    table.iterator.isEmpty should be (true)

    table.addOne("B", "A", 3) should be(Option.empty)
    table.addOne("B", "B", 4) should be(Option.empty)
    table.addOne("B", "B", 4) should be(Some(4))
    table.addOne("B", "B", 5) should be(Some(4))
    table.addOne("A", "A", 1) should be(Option.empty)
    table.addOne("A", "B", 2) should be(Option.empty)
    table.size should be (4)

    table.get("A", "A") should be(Some(1))
    table.get("A", "B") should be(Some(2))
    table.get("B", "A") should be(Some(3))
    table.get("B", "B") should be(Some(5))
    table.get("Foo", "Bar") should be(Option.empty)

    val row = Map[String, Int]("X" -> 42, "Y" -> 43)
    table.addAll("C", row)

    table.get("C", "X") should be(Some(42))
    table.getRow("C") should be(Some(row))
    table.size should be(6)

    table.remove("B", "X") should be(Option.empty)
    table.remove("B", "B") should be(Some(5))
    table.size should be(5)

    table.toString should be("HashMap(A -> HashMap(A -> 1, B -> 2), B -> HashMap(A -> 3), C -> HashMap(X -> 42, Y -> 43))")
  }
}