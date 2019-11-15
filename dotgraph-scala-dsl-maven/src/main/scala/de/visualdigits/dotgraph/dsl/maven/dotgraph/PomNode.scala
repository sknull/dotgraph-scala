package de.visualdigits.dotgraph.dsl.maven.dotgraph

import de.visualdigits.dotgraph.core.`type`.html.Align
import de.visualdigits.dotgraph.core.entity.html.{DotRow, DotTable}
import de.visualdigits.dotgraph.core.entity.{DotColor, DotEdge, DotGraph, DotNode}
import de.visualdigits.dotgraph.dsl.maven.model._
import de.visualdigits.dotgraph.dsl.maven.model.`type`.UpdateMode

import scala.collection.mutable

class PomNode(graph: PomGraph, val name: String, val pom: Pom, val isExternal: Boolean = false) extends DotNode(graph, name) {

  val table: DotTable = {
    val table: DotTable = DotTable().attributes
      .setBorder(0)
      .setCellborder(1)
      .setCellspacing(0)
      .setCellpadding(5)
      .entity
    attributes
      .setLabel(table)
      .setFontname("Verdana,Arial,Sans-Serif")
    table
  }

  def showAttributes(): Unit = {
    table.addRow("<b>%s [%s] =&gt; %s</b>".format(pom.artifact.resolveProperties().toString,
      pom.packaging,
      pom.parent.getOrElse("")),
      if (isExternal) DotColor.WHITE else DotColor.DEFAULT,
      if (isExternal) DotColor.BLUE else DotColor.BLUE_LIGHT,
      2, align = Align.center)
    showOrganization()
    showScm()
    showRepositories("REPOSITORIES", pom.repositories)
    showRepositories("DISTRIBUTION MANAGEMENT", pom.distributionManagement)
    showArtifacts()
  }

  private def showOrganization(): Unit = {
    val organization = pom.organization
    if (organization.nonEmpty) table.addRow("ORGANIZATION", colSpan = 2, align = Align.center)
      .addRow(DotRow()
        .addCell("Name", DotColor.DEFAULT, DotColor.GREY)
        .addCell(organization.get.name, DotColor.DEFAULT, DotColor.GREY_LIGHT))
      .addRow(DotRow()
        .addCell("URL", DotColor.DEFAULT, DotColor.GREY)
        .addCell(organization.get.url, DotColor.DEFAULT, DotColor.GREY_LIGHT))
  }

  private def showScm(): Unit = {
    val scm = pom.scm
    if (scm.nonEmpty) table.addRow("SCM", colSpan = 2, align = Align.center)
      .addRow(DotRow()
        .addCell("Connection", DotColor.DEFAULT, DotColor.GREY)
        .addCell(scm.get.connection, DotColor.DEFAULT, DotColor.GREY_LIGHT))
      .addRow(DotRow()
        .addCell("Developer Connection", DotColor.DEFAULT, DotColor.GREY)
        .addCell(scm.get.developerConnection, DotColor.DEFAULT, DotColor.GREY_LIGHT))
      .addRow(DotRow()
        .addCell("URL", DotColor.DEFAULT, DotColor.GREY)
        .addCell(scm.get.url, DotColor.DEFAULT, DotColor.GREY_LIGHT))
  }

  private def showRepositories(label: String, repositories: List[Repository]): Unit = {
    if (repositories.nonEmpty) {
      table.addRow(label, colSpan = 2, align = Align.center)
      repositories.foreach(repo => table.addRow(repo.toString, DotColor.DEFAULT, DotColor.GREY_LIGHT, 2, Align.center))
    }
  }

  private def showArtifacts(): Unit = {
    showProperties()
    val rows: mutable.ListBuffer[DotRow] = mutable.ListBuffer()
    rows ++= showDependencies("DEPENDENCY MANAGEMENT", pom.dependencyManagement.toMap)
    rows ++= showDependencies("DEPENDENCIES", pom.dependencies.toMap)
    pom.build.foreach(build => rows.addAll(showBuild(build)))
    rows.foreach(table.addRow(_))
    pom.profiles.foreach(showProfile)
  }

  private def showProperties(): Unit = {
    if (pom.properties.nonEmpty) {
      table.addRow("PROPERTIES", colSpan = 2, align = Align.center)
      pom.properties.filter(!_._2.derived).foreach(entry =>
        table.addRow(DotRow()
          .addCell(entry._1, DotColor.DEFAULT, DotColor.GREY)
          .addCell(entry._2.value, DotColor.DEFAULT, DotColor.GREY_LIGHT)))
    }
  }

  private def showProfile(profile: Profile): Unit = {
    table.addRow("PROFILE: " + profile.id, colSpan = 2, align = Align.center)
    profile.build.foreach(build => {
      showBuild(build).foreach(table.addRow(_))
    })
  }

  private def showBuild(build: Build): List[DotRow] = {
    val rows: mutable.ListBuffer[DotRow] = mutable.ListBuffer()
    build.resolveProperties()
    val hasFinalName = build.finalName.nonEmpty
    val hasSourceDirectory = build.sourceDirectory.nonEmpty
    val hasTestSourceDirectory = build.testSourceDirectory.nonEmpty
    if (hasFinalName || hasSourceDirectory || hasTestSourceDirectory || build.plugins.nonEmpty || build.pluginManagement.nonEmpty) {
      rows.addOne(DotRow("BUILD", colSpan = 2, align = Align.center))
    }
    if (hasFinalName) {
      rows.addOne(DotRow()
        .addCell("finalName", DotColor.DEFAULT, DotColor.GREY)
        .addCell(build.finalName, DotColor.DEFAULT, DotColor.GREY_LIGHT))
    }
    if (hasSourceDirectory) {
      rows.addOne(DotRow()
        .addCell("sourceDirectory", DotColor.DEFAULT, DotColor.GREY)
        .addCell(build.sourceDirectory, DotColor.DEFAULT, DotColor.GREY_LIGHT))
    }
    if (hasTestSourceDirectory) {
      rows.addOne(DotRow()
        .addCell("testSource", DotColor.DEFAULT, DotColor.GREY)
        .addCell(build.testSourceDirectory, DotColor.DEFAULT, DotColor.GREY_LIGHT))
    }
    if (build.resources.nonEmpty) {
      rows ++= showResources("RESOURCES", build.resources)
    }
    if (build.testResources.nonEmpty) {
      rows ++= showResources("TEST RESOURCES", build.testResources)
    }

    rows ++= showDependencies("PLUGIN MANAGEMENT", build.pluginManagement.toMap)
    rows ++= showDependencies("PLUGINS", build.plugins.toMap)
    rows.toList
  }

  private def showResources(label: String, resources: List[Resource]): List[DotRow] = {
    val rows: mutable.ListBuffer[DotRow] = mutable.ListBuffer()
    rows.addOne(DotRow(label, colSpan = 2, align = Align.center))
    resources.foreach(res => {
      rows.addOne(DotRow(res.directory, DotColor.DEFAULT, DotColor.GREY, 2))
      if (res.includes.nonEmpty) {
        rows.addOne(DotRow()
          .addCell("includes", DotColor.DEFAULT, DotColor.GREY)
          .addCell(res.includes.mkString("<br/>"), DotColor.DEFAULT, DotColor.GREY_LIGHT))
      }
      if (res.excludes.nonEmpty) {
        rows.addOne(DotRow()
          .addCell("excludes", DotColor.DEFAULT, DotColor.GREY)
          .addCell(res.excludes.mkString("<br/>"), DotColor.DEFAULT, DotColor.GREY_LIGHT))
      }
    })
    rows.toList
  }

  private def showDependencies(label: String, dependencies: Map[String, Artifact]): List[DotRow] = {
    val rows: mutable.ListBuffer[DotRow] = mutable.ListBuffer()
    graph
      .addCategory("Specified Artifact", Some(DotColor.GREEN_LIGHT))
      .addCategory("Resolved Artifact", Some(DotColor.YELLOW))
      .addCategory("Managed Artifact", Some(DotColor.YELLOW_LIGHT))
      .addCategory("UNRESOLVED ARTIFACT", Some(DotColor.RED_LIGHT))
      .addCategory("MISSING PROPERTY", Some(DotColor.RED))
    if (dependencies.nonEmpty) {
      rows.addOne(DotRow(label, colSpan = 2, align = Align.center))
      dependencies.values.filter(!_.derived).foreach(d => {
        d.resolveProperties()
        val color = d.updateMode match {
            case UpdateMode.none => if (d.version.isEmpty) DotColor.RED_LIGHT else if (d.version.contains("$")) DotColor.RED else DotColor.GREEN_LIGHT
            case UpdateMode.`resolved` => DotColor.YELLOW
            case UpdateMode.`management` => DotColor.YELLOW_LIGHT
          }
        rows.addOne(DotRow(d.toString, DotColor.DEFAULT, color, 2))
      })
    }
    rows.toList
  }

  override def createEdge(graph: DotGraph, from: DotNode, to: DotNode, label: String): DotEdge = {
    PomEdge(graph, from, to, label)
  }
}

object PomNode {
  def apply(graph: PomGraph, name: String, pom: Pom, isExternal: Boolean = false) = new PomNode(graph, name, pom, isExternal)
}
