package de.visualdigits.dotgraph.core.entity

import java.awt.Color

import de.visualdigits.dotgraph.core.`type`.DotColorComponent

/**
 * Represntation of a web color in the form #rrggbb.
 */
class DotColor(color: String = "") {

  val webColor: Boolean = color.nonEmpty && color.startsWith("#")
  var red: Int = if(webColor) Integer.valueOf(color.substring(1, 3), 16) else red
  var green: Int = if(webColor) Integer.valueOf(color.substring(3, 5), 16) else green
  var blue: Int = if(webColor) Integer.valueOf(color.substring(5, 7), 16) else blue

  private def this(red: Int, green: Int, blue: Int) {
    this
    this.red = red
    this.green = green
    this.blue = blue
  }

  override def toString: String = {
    if (!this.color.isEmpty) this.color
    else "#" + red.toHexString.padTo(2, '0') + green.toHexString.padTo(2, '0') + blue.toHexString.padTo(2, '0')
  }

  def multiply(component: DotColorComponent, factor: Double): DotColor = {
    val f = factor.toFloat
    val hsbvals = new Array[Float](3)
    Color.RGBtoHSB(red, green, blue, hsbvals)
    val hue = hsbvals(0)
    val saturation = hsbvals(1)
    val brightness = if (hsbvals(2) == 0) 1.0f - f
    else hsbvals(2)
    val c = component match {
      case DotColorComponent.RED =>
        new Color(Math.min(255, f * red), green, blue)
      case DotColorComponent.GREEN =>
        new Color(red, Math.min(255, f * green), blue)
      case DotColorComponent.BLUE =>
        new Color(red, green, Math.min(255, f * blue))
      case DotColorComponent.HUE =>
        Color.getHSBColor(Math.min(1.0f, f * hue), saturation, brightness)
      case DotColorComponent.SATURATION =>
        Color.getHSBColor(hue, Math.min(1.0f, f * saturation), brightness)
      case DotColorComponent.BRIGHTNESS =>
        Color.getHSBColor(hue, saturation, Math.min(1.0f, f * brightness))
      case _ =>
        new Color(red, green, blue)
    }
    new DotColor(c.getRed, c.getGreen, c.getBlue)
  }
}

object DotColor {
  def apply(color: String = ""): DotColor = {
    new DotColor(color)
  }

  val DEFAULT          = DotColor("")

  val RED           = DotColor("#ff0000")
  val RED_LIGHT     = DotColor("#ffdddd")
  val RED_DARK      = DotColor("#aa0000")
  val GREEN         = DotColor("#00ff00")
  val GREEN_LIGHT   = DotColor("#ddffdd")
  val GREEN_DARK    = DotColor("#00aa00")
  val BLUE          = DotColor("#0000ff")
  val BLUE_LIGHT    = DotColor("#ddddff")
  val BLUE_DARK     = DotColor("#0000aa")
  val CYAN          = DotColor("#00ffff")
  val CYAN_LIGHT    = DotColor("#ddffff")
  val CYAN_DARK     = DotColor("#00aaaa")
  val MAGENTA       = DotColor("#ff00ff")
  val MAGENTA_LIGHT = DotColor("#ffddff")
  val MAGENTA_DARK  = DotColor("#aa00aa")
  val YELLOW        = DotColor("#ffff00")
  val YELLOW_LIGHT  = DotColor("#ffffdd")
  val YELLOW_DARK   = DotColor("#aaaa00")
  val BLACK         = DotColor("#000000")
  val GREY          = DotColor("#cccccc")
  val GREY_LIGHT    = DotColor("#dddddd")
  val GREY_DARK     = DotColor("#444444")
  val WHITE         = DotColor("#ffffff")
}
