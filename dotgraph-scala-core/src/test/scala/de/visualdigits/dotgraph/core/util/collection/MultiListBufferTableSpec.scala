package de.visualdigits.dotgraph.core.util.collection

import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.junit.JUnitRunner

import scala.collection.mutable

@RunWith(classOf[JUnitRunner])
class MultiListBufferTableSpec extends AnyFlatSpec with Matchers {

  "A MultiLinkedHashSetTableSpec" should "behave as expected" in {
    val table = MultiListBufferTable[String, String, Int]()

    table.addOneValue("A", "A", 1) should be(Option.empty)
    table.addOneValue("A", "B", 2) should be(Option.empty)
    table.addOneValue("B", "A", 3) should be(Option.empty)
    table.addOneValue("B", "B", 4) should be(Option.empty)
    table.addOneValue("B", "B", 4) should be(Some(4))
    table.get("B", "B") should be(Some(mutable.ListBuffer(4, 4)))

    table.addOneValue("B", "B", 5) should be(Some(4))
    table.get("B", "B") should be(Some(mutable.ListBuffer(4, 4, 5)))

    table.size() should be(6)

    table.toString should be ("HashMap(A -> HashMap(A -> ListBuffer(1), B -> ListBuffer(2)), B -> HashMap(A -> ListBuffer(3), B -> ListBuffer(4, 4, 5)))")
  }
}
