package de.visualdigits.dotgraph.dsl.maven.model

trait MavenObject {

  def subst(s: String, m: Map[String, PropertyValue]): String = {
    m.foldLeft(s){ case (newState, kv) => newState.replace('$' + s"{${kv._1}}", kv._2.value)}
  }
}
