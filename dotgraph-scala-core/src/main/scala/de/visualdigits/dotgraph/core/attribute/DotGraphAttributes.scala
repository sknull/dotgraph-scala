package de.visualdigits.dotgraph.core.attribute

import de.visualdigits.dotgraph.core.`type`._
import de.visualdigits.dotgraph.core.entity.{DotColor, DotGraph}

/**
 * Attributes for graphs as described
 * <a  href="https://graphviz.gitlab.io/_pages/doc/info/attrs.html">here</a>
 *
 * @param entity The entity to be returned from the fluent calls (see examples).
 */
class DotGraphAttributes(entity: DotGraph) extends AbstractDotAttributes[DotGraph, DotGraphAttributes](entity) {

  override def toString: String = super.repr(";\n  ")

  def getColor: DotColor = this.attributes.get("color").map(_.asInstanceOf[DotColor]).orNull

  def setColor(color: DotColor): DotGraphAttributes = {
    this.attributes.put("color", color)
    this
  }

  def getFillcolor: DotColor = this.attributes.get("fillcolor").map(_.asInstanceOf[DotColor]).orNull

  def setFillcolor(color: DotColor): DotGraphAttributes = {
    this.attributes.put("fillcolor", color)
    this
  }

  def getColorscheme: String = this.attributes.get("colorscheme").map(_.asInstanceOf[String]).orNull

  def setColorscheme(colorscheme: String): DotGraphAttributes = {
    this.attributes.put("colorscheme", colorscheme)
    this
  }

  def getStyle: ClusterStyle = this.attributes.get("style").map(_.asInstanceOf[ClusterStyle]).orNull

  def setStyle(style: ClusterStyle): DotGraphAttributes = {
    this.attributes.put("style", style)
    this
  }

  def getComment: String = this.attributes.get("comment").map(_.asInstanceOf[String]).orNull

  def setComment(comment: String): DotGraphAttributes = {
    this.attributes.put("comment", comment)
    this
  }

  def getFontcolor: DotColor = this.attributes.get("fontcolor").map(_.asInstanceOf[DotColor]).orNull

  def setFontcolor(fontcolor: DotColor): DotGraphAttributes = {
    this.attributes.put("fontcolor", fontcolor)
    this
  }

  def getFontname: String = this.attributes.get("fontname").map(_.asInstanceOf[String]).orNull

  def setFontname(fontname: String): DotGraphAttributes = {
    this.attributes.put("fontname", fontname)
    this
  }

  def getFontsize: Double = this.attributes.get("fontsize").asInstanceOf[Double]

  def setFontsize(fontsize: Double): DotGraphAttributes = {
    this.attributes.put("fontsize", fontsize)
    this
  }

  def getLabel: String = this.attributes.get("label").map(_.asInstanceOf[String]).orNull

  def setLabel(label: String): DotGraphAttributes = {
    this.attributes.put("label", label)
    this
  }

  def getTarget: String = this.attributes.get("target").map(_.asInstanceOf[String]).orNull

  def setTarget(target: String): DotGraphAttributes = {
    this.attributes.put("target", target)
    this
  }

  def isCenter: Boolean = this.attributes.get("center").asInstanceOf[Boolean]

  def setCenter(center: Boolean): DotGraphAttributes = {
    this.attributes.put("center", center)
    this
  }

  def getCharset: String = this.attributes.get("charset").map(_.asInstanceOf[String]).orNull

  def setCharset(charset: String): DotGraphAttributes = {
    this.attributes.put("charset", charset)
    this
  }

  def isConcentrate: Boolean = this.attributes.get("concentrate").asInstanceOf[Boolean]

  def setConcentrate(concentrate: Boolean): DotGraphAttributes = {
    this.attributes.put("concentrate", concentrate)
    this
  }

  def getDiredgeconstraints: String = this.attributes.get("diredgeconstraints").map(_.asInstanceOf[String]).orNull

  def setDiredgeconstraints(diredgeconstraints: String): DotGraphAttributes = {
    this.attributes.put("diredgeconstraints", diredgeconstraints)
    this
  }

  def getDpi: Double = this.attributes.get("dpi").asInstanceOf[Double]

  def setDpi(dpi: Double): DotGraphAttributes = {
    this.attributes.put("dpi", dpi)
    this
  }

  def getEpsilon: Double = this.attributes.get("epsilon").asInstanceOf[Double]

  def setEpsilon(epsilon: Double): DotGraphAttributes = {
    this.attributes.put("epsilon", epsilon)
    this
  }

  def getEsep: Double = this.attributes.get("esep").asInstanceOf[Double]

  def setEsep(esep: Double): DotGraphAttributes = {
    this.attributes.put("esep", esep)
    this
  }

  def getFontpath: String = this.attributes.get("fontpath").map(_.asInstanceOf[String]).orNull

  def setFontpath(fontpath: String): DotGraphAttributes = {
    this.attributes.put("fontpath", fontpath)
    this
  }

  def isForcelabels: Boolean = this.attributes.get("forcelabels").asInstanceOf[Boolean]

  def setForcelabels(forcelabels: Boolean): DotGraphAttributes = {
    this.attributes.put("forcelabels", forcelabels)
    this
  }

  def getImagepath: String = this.attributes.get("imagepath").map(_.asInstanceOf[String]).orNull

  def setImagepath(imagepath: String): DotGraphAttributes = {
    this.attributes.put("imagepath", imagepath)
    this
  }

  def getLayers: String = this.attributes.get("layers").map(_.asInstanceOf[String]).orNull

  def setLayers(layers: String): DotGraphAttributes = {
    this.attributes.put("layers", layers)
    this
  }

  def getLayerselect: String = this.attributes.get("layerselect").map(_.asInstanceOf[String]).orNull

  def setLayerselect(layerselect: String): DotGraphAttributes = {
    this.attributes.put("layerselect", layerselect)
    this
  }

  def getLayersep: String = this.attributes.get("layersep").map(_.asInstanceOf[String]).orNull

  def setLayersep(layersep: String): DotGraphAttributes = {
    this.attributes.put("layersep", layersep)
    this
  }

  def getLayout: String = this.attributes.get("layout").map(_.asInstanceOf[String]).orNull

  def setLayout(layout: String): DotGraphAttributes = {
    this.attributes.put("layout", layout)
    this
  }

  def getMaxiter: Integer = this.attributes.get("maxiter").asInstanceOf[Int]

  def setMaxiter(maxiter: Integer): DotGraphAttributes = {
    this.attributes.put("maxiter", maxiter)
    this
  }

  def getNodesep: Double = this.attributes.get("nodesep").asInstanceOf[Double]

  def setNodesep(nodesep: Double): DotGraphAttributes = {
    this.attributes.put("nodesep", nodesep)
    this
  }

  def getNormalize: Double = this.attributes.get("normalize").asInstanceOf[Double]

  def setNormalize(normalize: Double): DotGraphAttributes = {
    this.attributes.put("normalize", normalize)
    this
  }

  def getOrientation: String = this.attributes.get("orientation").map(_.asInstanceOf[String]).orNull

  def setOrientation(orientation: String): DotGraphAttributes = {
    this.attributes.put("orientation", orientation)
    this
  }

  def getOutputorder: OutputMode = this.attributes.get("outputorder").map(_.asInstanceOf[OutputMode]).orNull

  def setOutputorder(outputorder: OutputMode): DotGraphAttributes = {
    this.attributes.put("outputorder", outputorder)
    this
  }

  def getOverlap: String = this.attributes.get("overlap").map(_.asInstanceOf[String]).orNull

  def setOverlap(overlap: String): DotGraphAttributes = {
    this.attributes.put("overlap", overlap)
    this
  }

  def isPack: Boolean = this.attributes.get("pack").asInstanceOf[Boolean]

  def setPack(pack: Boolean): DotGraphAttributes = {
    this.attributes.put("pack", pack)
    this
  }

  def getPackmode: PackMode = this.attributes.get("packmode").map(_.asInstanceOf[PackMode]).orNull

  def setPackmode(packmode: PackMode): DotGraphAttributes = {
    this.attributes.put("packmode", packmode)
    this
  }

  def getPad: Point = this.attributes.get("pad").map(_.asInstanceOf[Point]).orNull

  def setPad(pad: Point): DotGraphAttributes = {
    this.attributes.put("pad", pad)
    this
  }

  def getPage: Point = this.attributes.get("page").map(_.asInstanceOf[Point]).orNull

  def setPage(page: Point): DotGraphAttributes = {
    this.attributes.put("page", page)
    this
  }

  def getPagedir: PageDir = this.attributes.get("pagedir").map(_.asInstanceOf[PageDir]).orNull

  def setPagedir(pagedir: PageDir): DotGraphAttributes = {
    this.attributes.put("pagedir", pagedir)
    this
  }

  def getQuantum: Double = this.attributes.get("quantum").asInstanceOf[Double]

  def setQuantum(quantum: Double): DotGraphAttributes = {
    this.attributes.put("quantum", quantum)
    this
  }

  def getRanksep: Double = this.attributes.get("ranksep").asInstanceOf[Double]

  def setRanksep(ranksep: Double): DotGraphAttributes = {
    this.attributes.put("ranksep", ranksep)
    this
  }

  def getRatio: Point = this.attributes.get("ratio").map(_.asInstanceOf[Point]).orNull

  def setRatio(ratio: Point): DotGraphAttributes = {
    this.attributes.put("ratio", ratio)
    this
  }

  def getResolution: Double = this.attributes.get("resolution").asInstanceOf[Double]

  def setResolution(resolution: Double): DotGraphAttributes = {
    this.attributes.put("resolution", resolution)
    this
  }

  def getRotate: Integer = this.attributes.get("rotate").asInstanceOf[Int]

  def setRotate(rotate: Integer): DotGraphAttributes = {
    this.attributes.put("rotate", rotate)
    this
  }

  def getScale: Point = this.attributes.get("scale").map(_.asInstanceOf[Point]).orNull

  def setScale(scale: Point): DotGraphAttributes = {
    this.attributes.put("scale", scale)
    this
  }

  def getSep: Double = this.attributes.get("sep").asInstanceOf[Double]

  def setSep(sep: Double): DotGraphAttributes = {
    this.attributes.put("sep", sep)
    this
  }

  def getSize: Point = this.attributes.get("size").map(_.asInstanceOf[Point]).orNull

  def setSize(size: Point): DotGraphAttributes = {
    this.attributes.put("size", size)
    this
  }

  def isSplines: Boolean = this.attributes.get("splines").asInstanceOf[Boolean]

  def setSplines(splines: Boolean): DotGraphAttributes = {
    this.attributes.put("splines", splines)
    this
  }

  def getBgcolor: DotColor = this.attributes.get("bgcolor").map(_.asInstanceOf[DotColor]).orNull

  def setBgcolor(bgcolor: DotColor): DotGraphAttributes = {
    this.attributes.put("bgcolor", bgcolor)
    this
  }

  def getLabeljust: String = this.attributes.get("labeljust").map(_.asInstanceOf[String]).orNull

  def setLabeljust(labeljust: String): DotGraphAttributes = {
    this.attributes.put("labeljust", labeljust)
    this
  }

  def getSortv: Integer = this.attributes.get("sortv").asInstanceOf[Int]

  def setSortv(sortv: Integer): DotGraphAttributes = {
    this.attributes.put("sortv", sortv)
    this
  }

  def isNojustify: Boolean = this.attributes.get("nojustify").asInstanceOf[Boolean]

  def setNojustify(nojustify: Boolean): DotGraphAttributes = {
    this.attributes.put("nojustify", nojustify)
    this
  }

  def getRoot: String = this.attributes.get("root").map(_.asInstanceOf[String]).orNull

  def setRoot(root: String): DotGraphAttributes = {
    this.attributes.put("root", root)
    this
  }

  def getGradientangle: Integer = this.attributes.get("gradientangle").asInstanceOf[Int]

  def setGradientangle(gradientangle: Integer): DotGraphAttributes = {
    this.attributes.put("gradientangle", gradientangle)
    this
  }

  def getMargin: Double = this.attributes.get("margin").asInstanceOf[Double]

  def setMargin(margin: Double): DotGraphAttributes = {
    this.attributes.put("margin", margin)
    this
  }

  def getLabelloc: String = this.attributes.get("labelloc").map(_.asInstanceOf[String]).orNull

  def setLabelloc(labelloc: String): DotGraphAttributes = {
    this.attributes.put("labelloc", labelloc)
    this
  }

  def getRankdir: RankDir = this.attributes.get("rankdir").map(_.asInstanceOf[RankDir]).orNull

  def setRankdir(rankdir: RankDir): DotGraphAttributes = {
    this.attributes.put("rankdir", rankdir)
    this
  }
}

object DotGraphAttributes {
  def apply(entity: DotGraph) = new DotGraphAttributes(entity)
}
