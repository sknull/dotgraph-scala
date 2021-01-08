package de.visualdigits.dotgraph.dsl.sbt

import java.io.File

import com.twitter.util.Eval
import org.scalatest.FunSuite

class SbtTest extends FunSuite {

  test("compile") {
    val eval = new Eval()
    val compiledCode = eval.apply(new File("/Users/knull/git/inbound/inbound-commons/build.sbt"))
    println(compiledCode)
  }
}
