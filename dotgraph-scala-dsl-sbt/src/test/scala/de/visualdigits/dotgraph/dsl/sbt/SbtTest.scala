package de.visualdigits.dotgraph.dsl.sbt

import java.io.File
import com.twitter.util.Eval
import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SbtTest extends AnyFunSuite {

  ignore("compile") {
    val eval = new Eval()
    val compiledCode = eval.apply(new File("/Users/knull/git/inbound/inbound-commons/build.sbt"))
    println(compiledCode)
  }
}
