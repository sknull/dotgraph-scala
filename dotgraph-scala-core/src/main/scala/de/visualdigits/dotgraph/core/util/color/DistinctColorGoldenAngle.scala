package de.visualdigits.dotgraph.core.util.color

import java.awt.Color

import de.visualdigits.dotgraph.core.entity.DotColor

import scala.util.Random

/**
 * Disitinct color generator which makes use of the golden angle to provide unique
 * colors around the color wheel.
 */
object DistinctColorGoldenAngle extends DistinctColor {

  private[this] val GOLDEN_ANGLE_OFFSET: Float = 137.5f

  private[this] val random: Random = Random

  private[this] var angleCurrent: Float = random.nextFloat() * 360.0f

  override def getNextColor(saturation: Float = 0.3f, brightness: Float = 1.0f): DotColor = {
    angleCurrent += GOLDEN_ANGLE_OFFSET
    val angle = angleCurrent % 360.0f / 360.0f // normalize to 0.0 - 1.0
    val color = new Color(Color.HSBtoRGB(angle, saturation, brightness))
    DotColor("#%s%s%s".format(color.getRed.toHexString, color.getGreen.toHexString, color.getBlue.toHexString))
  }
}
