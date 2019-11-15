package de.visualdigits.dotgraph.dsl.maven.model

import java.io.File
import java.nio.file.Paths

import scala.collection.mutable
import scala.xml.{Node, XML}

class Pom(val pomFile: File, val parentPom: Option[Pom] = Option.empty) extends MavenObject {

  val childPoms: mutable.Set[Pom] = {
    parentPom.foreach(_.childPoms.addOne(this))
    mutable.Set()
  }
  val root: Node = XML.loadFile(pomFile)
  val modelVersion: String = (root \ "modelVersion").map(_.text).headOption.getOrElse("")
  val packaging: String = (root \ "packaging").map(_.text).headOption.getOrElse("jar")
  val artifact = Artifact(root, this)
  val parent: Option[Artifact] = (root \ "parent").map(Artifact(_, this)).headOption
  val organization: Option[Organization] = (root \ "organization").map(Organization(_)).headOption
  val scm: Option[Scm] = (root \ "scm").map(Scm(_)).headOption
  val repositories: List[Repository] = (root \ "repositories" \ "repository").map(Repository(_)).toList
  val distributionManagement: List[Repository] = {
    val repos = mutable.ListBuffer[Repository]()
    repos.addAll((root \ "distributionManagement" \ "repository").map(Repository(_)))
    repos.addAll((root \ "distributionManagement" \ "snapshotRepository").map(SnapshotRepository(_)))
    repos.toList
  }
  val modules: mutable.Set[Pom] = mutable.Set()
  (root \\ "module").foreach(module => {
    val modulePomFile = Paths.get(pomFile.getParent, module.text, "pom.xml").toFile
    if (modulePomFile.exists()) modules.addOne(Pom(modulePomFile, Some(this)))
  })
  val properties: mutable.Map[String, PropertyValue] = mutable.TreeMap[String, PropertyValue]().addAll((root \ "properties").flatMap(_.child.filter(_.text.trim.nonEmpty).map(pp => (pp.label, PropertyValue(pp.text.trim)))))
  val dependencyManagement: mutable.Map[String, Dependency] = {
    mutable.Map().addAll((root \ "dependencyManagement" \ "dependencies" \ "dependency").map(d => {
      val dep = Dependency(d, this)
      (dep.key, dep)
    }))
  }
  val dependencies: mutable.Map[String, Dependency] = {
    mutable.Map().addAll((root \ "dependencies" \ "dependency").map(d => {
      val dep = Dependency(d, this)
      (dep.key, dep)
    }))
  }
  val build: Option[Build] = (root \ "build").map(Build(_, this)).headOption
  val profiles: List[Profile] = (root \ "profiles" \ "profile").map(Profile(_, this)).toList

  var visited: Boolean = false

  def dfs(node: Pom = this, dfsSet: mutable.LinkedHashSet[Pom] = mutable.LinkedHashSet[Pom]()): mutable.LinkedHashSet[Pom] = {
    if (!node.visited) {
      node.visited = true
      node.childPoms.foreach(reference => dfs(reference, dfsSet))
      dfsSet.add(node)
    }
    dfsSet
  }
}

object Pom {
  def apply(pomFile: File, parentPom: Option[Pom] = Option.empty) = new Pom(pomFile, parentPom)
}
