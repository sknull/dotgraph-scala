package de.visualdigits.dotgraph.dsl.sbt

import java.io.File
import java.util.regex.Pattern

class Sbt {
}

object Sbt {
  val DEPENDENCY_PATTERN = Pattern.compile("(\".+\" %%? \".+\" %%? [\"\\w.-]+)")

  def apply(sbtFile: File): Sbt = new Sbt()
}
