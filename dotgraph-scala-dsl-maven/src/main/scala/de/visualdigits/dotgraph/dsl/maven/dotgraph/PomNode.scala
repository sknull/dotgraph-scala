package de.visualdigits.dotgraph.dsl.maven.dotgraph

import de.visualdigits.dotgraph.core.`type`.html.Align
import de.visualdigits.dotgraph.core.entity.html.{DotRow, DotTable}
import de.visualdigits.dotgraph.core.entity.{DotColor, DotEdge, DotGraph, DotNode}
import de.visualdigits.dotgraph.dsl.maven.model.pom.`type`.UpdateMode
import de.visualdigits.dotgraph.dsl.maven.model.pom.artifact.Artifact
import de.visualdigits.dotgraph.dsl.maven.model.pom.repository.Repository
import de.visualdigits.dotgraph.dsl.maven.model.pom.{Build, Pom, Profile, Resource}

import scala.collection.mutable

class PomNode(
               graph: PomGraph,
               var name: String,
               val pom: Pom, m2RepoDirectory: String,
               val isExternal: Boolean = false,
               var bgColor: DotColor = DotColor.DEFAULT,
               analyzeDependencies: Boolean = false
             ) extends DotNode(graph, name) {

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

  pom.pomNode = Some(this)

  updateCoordinatesFromHierarchy()
  showAttributes()
  id = pom.artifact.key
  name = id

  def resolveDependencyPoms(graph: PomGraph, m2RepoDirectory: String, filterPackages: Seq[String] = Seq()): Seq[PomNode] = {
    addNode(graph, this, filterPackages)
    val addedChilds = mutable.ListBuffer[PomNode]()
    pom.dependencies.values
      .foreach(dep => {
        dep.resolveProperties(pom.consolidatedProperties.toMap)
        if (analyzeDependencies) {
          val key = dep.key
          val p = dep.loadPomFromLocalRepo(m2RepoDirectory)
          p.map((pom: Pom) =>
            PomNode(graph, key, pom, m2RepoDirectory, bgColor = DotColor.YELLOW_LIGHT, analyzeDependencies = analyzeDependencies))
            .foreach(pn => {
              pn.resolveDependencyPoms(graph, m2RepoDirectory, filterPackages)
              addNode(graph, pn, filterPackages)
              graph.addEdgeById(id, pn.id, color = DotColor.GREEN_DARK)
              addedChilds.addOne(pn)
            })
        }
      })
    addedChilds.toSeq
  }

  private def addNode(graph: PomGraph, node: PomNode, filterPackages: Seq[String]): Option[DotNode] = {
    var graphNode = graph.getNodeById(node.id)
    if (graphNode.isEmpty && (filterPackages.isEmpty || filterPackages.exists(node.pom.artifact.groupId.startsWith(_)))) {
      graphNode = Some(graph.addAndReturnNode(node))
    }
    graphNode
  }

  def updateCoordinatesFromHierarchy(): Unit = {
    val properties = pom.consolidatedProperties.toMap

    pom.parent.foreach(pom.artifact.update(_, UpdateMode.none))

    pom.dependencyManagement.values.foreach(dm => {
      dm.resolveProperties(properties)
      pom.dependencies.get(dm.key).foreach(dep => {
        dep.update(dm, UpdateMode.management)
      })
    })
    refreshKeys(pom.dependencyManagement)

    pom.dependencies.values.foreach(d => {
      d.resolveProperties(properties)
    })
    refreshKeys(pom.dependencies)

    pom.build.foreach(build => {
      build.pluginManagement.values.foreach(pm => {
        pm.resolveProperties(properties)
        build.plugins.get(pm.key).foreach(p => {
          p.update(pm, UpdateMode.management)
        })
      })
      refreshKeys(build.pluginManagement)
      build.plugins.values.foreach(p => {
        p.resolveProperties(properties)
      })
      refreshKeys(build.plugins)
    })

    pom.childPoms.foreach(child => {
      child.artifact.update(pom.artifact, UpdateMode.none)

      // derive properties
      // only override values which either do not exist in the poms properties or are empty
      pom.properties.foreach(elem => {
        val value = child.properties.get(elem._1)
        if (value.isEmpty || value.get.isEmpty) {
          val p = elem._2.clone()
          p.derived = true
          child.properties.addOne(elem._1, p)
        }
      })

      // process dependency management node (if any)
      child.dependencyManagement.values.foreach(_.resolveProperties(properties))
      refreshKeys(child.dependencyManagement)

      // process dependencies node (if any)
      child.dependencies.values.foreach(d => d.resolveProperties(properties))
      refreshKeys(child.dependencies)

      // derive dependency management
      pom.dependencyManagement.foreach(elem => {
        val dep = child.dependencyManagement.get(elem._2.key)
        if (dep.nonEmpty) dep.get.update(elem._2, UpdateMode.resolved)
        else {
          val dep = elem._2.clone()
          dep.derived = true
          child.dependencyManagement.addOne(elem._1, dep)
        }
      })

      // resolve dependencies from management
      child.dependencyManagement.values.foreach(d => {
        child.dependencies.get(d.key).foreach(dep => {
          dep.update(d, UpdateMode.management)
        })
      })

      // process build node (if any)
      pom.build.foreach(pb => {
        child.build.foreach(cb => {
          // process plugin management node (if any)
          cb.pluginManagement.values.foreach(_.resolveProperties(properties))
          refreshKeys(cb.pluginManagement)

          // process plugins node (if any)
          cb.plugins.values.foreach(_.resolveProperties(properties))
          refreshKeys(cb.plugins)

          // derive plugin management
          pb.pluginManagement.foreach(pe => {
            val p = cb.pluginManagement.get(pe._2.key)
            if (p.nonEmpty) p.get.update(pe._2, UpdateMode.resolved)
            else {
              val p = pe._2.clone()
              p.derived = true
              cb.pluginManagement.addOne(pe._1, p)
            }
          })

          // resolve plugins from management
          cb.pluginManagement.values.foreach(d => {
            cb.plugins.get(d.key).foreach(p => {
              p.update(d, UpdateMode.management)
            })
          })
        })
      })
    })
  }

  /**
   * Recalculates the keys of the given dependencies by re assigning the values to their current keys.
   *
   * @param dependencies The dependencies.
   * @tparam T
   */
  def refreshKeys[T <: Artifact](dependencies: mutable.Map[String, T]): Unit = {
    val artifacts: mutable.Map[String, T] = mutable.Map()
    dependencies.values.foreach(dm => {
      artifacts.addOne(dm.key, dm)
    })
    dependencies.clear()
    dependencies.addAll(artifacts.asInstanceOf[mutable.Map[String, T]])
    artifacts.clear()
  }

  def showAttributes(): Unit = {
    val title = "<b>%s</b>".format(pom.artifact.resolveProperties(pom.consolidatedProperties.toMap).toString)
    table.clearRows()
    table.addRow(title, DotColor.DEFAULT, bgColor,
      2, align = Align.center)
//    showOrganization()
//    showScm()
//    showRepositories("REPOSITORIES", pom.repositories)
//    showRepositories("DISTRIBUTION MANAGEMENT", pom.distributionManagement)
//    showArtifacts()
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
        d.resolveProperties(pom.consolidatedProperties.toMap)
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
  def apply(
             graph: PomGraph,
             name: String,
             pom: Pom,
             m2RepoDirectory: String,
             isExternal: Boolean = false,
             bgColor: DotColor = DotColor.DEFAULT,
             analyzeDependencies: Boolean = false
           ) = new PomNode(graph, name, pom, m2RepoDirectory, isExternal, bgColor, analyzeDependencies)
}
