package de.visualdigits.dotgraph.core.util.color

import de.visualdigits.dotgraph.core.entity.DotColor

/**
 * Interface for unique colors as used to colorize graphs or legends for a graph.
 */
trait DistinctColor {
  def getNextColor(saturation: Float = 1.0f, brightness: Float = 1.0f): DotColor
}
