package de.visualdigits.dotgraph.core.entity

import java.awt.Color

import de.visualdigits.dotgraph.core.`type`.DotColorComponent

/**
 * Representation of a web color in the form #rrggbb.
 *
 * @param color The color name or webcolor in the form #rrggbb.
 * @param hsb The color in Hue (0.0 - 360.0), Saturation (0.0 - 1.0) and Brightness (0.0 - 1.0).
 * @param name The color name.
 */
class DotColor(color: String = "", hsb: Option[(Float, Float, Float)] = None, name: String = "web") {

  val webColor: Boolean = color.nonEmpty && color.startsWith("#")
  var red: Int = if(webColor) Integer.valueOf(color.substring(1, 3), 16) else red
  var green: Int = if(webColor) Integer.valueOf(color.substring(3, 5), 16) else green
  var blue: Int = if(webColor) Integer.valueOf(color.substring(5, 7), 16) else blue
  hsb.map(h => {
    Color.getHSBColor(h._1 / 360.0f, h._2, h._3)
  })

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

  /**
   * Multiplies the given color component with the given factor to midify the current color.
   *
   * @param component The color component to change.
   * @param factor The multiplier.
   *
   * @return DotColor
   */
  def multiply(component: DotColorComponent, factor: Float): DotColor = {
    val f = factor.toFloat
    val hsbvals = new Array[Float](3)
    Color.RGBtoHSB(red, green, blue, hsbvals)
    val hue = hsbvals(0)
    val saturation = hsbvals(1)
    val brightness = if (hsbvals(2) == 0) 1.0f - f else hsbvals(2)
    val c = component match {
      case DotColorComponent.RED => new Color(Math.min(255, f * red), green, blue)
      case DotColorComponent.GREEN => new Color(red, Math.min(255, f * green), blue)
      case DotColorComponent.BLUE => new Color(red, green, Math.min(255, f * blue))
      case DotColorComponent.HUE => Color.getHSBColor(Math.min(1.0f, f * hue), saturation, brightness)
      case DotColorComponent.SATURATION => Color.getHSBColor(hue, Math.min(1.0f, f * saturation), brightness)
      case DotColorComponent.BRIGHTNESS => Color.getHSBColor(hue, saturation, Math.min(1.0f, f * brightness))
      case _ => new Color(red, green, blue)
    }
    new DotColor(c.getRed, c.getGreen, c.getBlue)
  }

  /**
   * Sets the given color component to the given value. The value has to be suitable for the component:<br/>
   * <ul>
   *   <li>RED, GREEN, BLUE: 0 - 255</li>
   *   <li>HUE: 0.0 - 360.0</li>
   *   <li>SATURATION, BRIGHTNESS: 0.0 - 1.0</li>
   * </ul>
   *
   * @param component The color component to set.
   * @param value The value to set (see above).
   *
   * @return DotColor
   */
  def set(component: DotColorComponent, value: Float): DotColor = {
    val hsb = new Array[Float](3)
    Color.RGBtoHSB(red, green, blue, hsb)
    val c = component match {
      case DotColorComponent.RED => new Color(value.toInt, green, blue)
      case DotColorComponent.GREEN => new Color(red, value, blue)
      case DotColorComponent.BLUE => new Color(red, green, value)
      case DotColorComponent.HUE => Color.getHSBColor(value / 360.0f, hsb(1), hsb(2))
      case DotColorComponent.SATURATION => Color.getHSBColor(hsb(0), value, hsb(2))
      case DotColorComponent.BRIGHTNESS => Color.getHSBColor(hsb(0), hsb(1), value)
      case _ => new Color(red, green, blue)
    }
    new DotColor(c.getRed, c.getGreen, c.getBlue)
  }
}

object DotColor {
  def apply(color: String = "", hsb: Option[(Float, Float, Float)] = None, name: String = "web"): DotColor = new DotColor(color, hsb, name)

  val DEFAULT       = DotColor(""       , name = "DEFAULT")

  val RED           = DotColor("#ff0000", name = "RED")
  val RED_LIGHT     = DotColor("#ffdddd", name = "RED_LIGHT")
  val RED_DARK      = DotColor("#aa0000", name = "RED_DARK")
  val GREEN         = DotColor("#00ff00", name = "GREEN")
  val GREEN_LIGHT   = DotColor("#ddffdd", name = "GREEN_LIGHT")
  val GREEN_DARK    = DotColor("#00aa00", name = "GREEN_DARK")
  val BLUE          = DotColor("#0000ff", name = "BLUE")
  val BLUE_LIGHT    = DotColor("#ddddff", name = "BLUE_LIGHT")
  val BLUE_DARK     = DotColor("#0000aa", name = "BLUE_DARK")
  val CYAN          = DotColor("#00ffff", name = "CYAN")
  val CYAN_LIGHT    = DotColor("#ddffff", name = "CYAN_LIGHT")
  val CYAN_DARK     = DotColor("#00aaaa", name = "CYAN_DARK")
  val MAGENTA       = DotColor("#ff00ff", name = "MAGENTA")
  val MAGENTA_LIGHT = DotColor("#ffddff", name = "MAGENTA_LIGHT")
  val MAGENTA_DARK  = DotColor("#aa00aa", name = "MAGENTA_DARK")
  val YELLOW        = DotColor("#ffff00", name = "YELLOW")
  val YELLOW_LIGHT  = DotColor("#ffffdd", name = "YELLOW_LIGHT")
  val YELLOW_DARK   = DotColor("#aaaa00", name = "YELLOW_DARK")
  val BLACK         = DotColor("#000000", name = "BLACK")
  val GREY          = DotColor("#cccccc", name = "GREY")
  val GREY_LIGHT    = DotColor("#dddddd", name = "GREY_LIGHT")
  val GREY_DARK     = DotColor("#444444", name = "GREY_DARK")
  val WHITE         = DotColor("#ffffff", name = "WHITE")
}
