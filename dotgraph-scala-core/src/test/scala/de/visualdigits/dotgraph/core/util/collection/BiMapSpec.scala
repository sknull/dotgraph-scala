package de.visualdigits.dotgraph.core.util.collection

import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BiMapSpec extends AnyFlatSpec with Matchers {

  "A BiMap" should "behave as expected" in {
    val map = BiMap[String, Int]()

    map.addOne("A", 123)
    map.addOne("B", 42)

    map.get("A") should be(Some(123))
    map.get("B") should be(Some(42))
    map.get("Foo") should be(Option.empty)

    map.lookup(123) should be(Some("A"))
    map.lookup(42) should be(Some("B"))

    map.size should be(2)

    map.addOne("B", 4711)
    map.get("B") should be(Some(4711))
    map.lookup(42) should be(Option.empty)
    map.lookup(4711) should be(Some("B"))
  }
}
