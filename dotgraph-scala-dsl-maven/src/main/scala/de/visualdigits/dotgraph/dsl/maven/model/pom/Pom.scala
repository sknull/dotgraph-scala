package de.visualdigits.dotgraph.dsl.maven.model.pom

import java.io.{File, InputStream}
import java.nio.file.Paths

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.visualdigits.dotgraph.dsl.maven.dotgraph.PomNode
import de.visualdigits.dotgraph.dsl.maven.model.common.{MavenObject, PropertyValue}
import de.visualdigits.dotgraph.dsl.maven.model.pom.artifact.{Artifact, Dependency}
import de.visualdigits.dotgraph.dsl.maven.model.pom.repository.{Repository, SnapshotRepository}

import scala.collection.mutable
import scala.xml.{Node, XML}

@JsonIgnoreProperties(Array("root", "parentPom", "childPoms", "pomFile"))
class Pom(val root: Node, m2RepoDirectory: String, val pomFile: Option[File], var parentPom: Option[Pom] = Option.empty, var pomNode: Option[PomNode]) extends MavenObject(root, null) {

  val childPoms: mutable.Set[Pom] = {
    parentPom.foreach(_.childPoms.addOne(this))
    mutable.Set()
  }
  val modelVersion: String = (root \ "modelVersion").map(_.text).headOption.getOrElse("")
  val packaging: String = (root \ "packaging").map(_.text).headOption.getOrElse("jar")
  val artifact: Artifact = Artifact(root, this)
  val parent: Option[Artifact] = (root \ "parent").map(Artifact(_, this)).headOption
  if (parentPom.isEmpty && parent.nonEmpty) parentPom = parent.get.loadPomFromLocalRepo(m2RepoDirectory)
  val organization: Option[Organization] = (root \ "organization").map(Organization(_, this)).headOption
  val scm: Option[Scm] = (root \ "scm").map(Scm(_, this)).headOption
  val repositories: List[Repository] = (root \ "repositories" \ "repository").map(Repository(_, this)).toList
  val distributionManagement: List[Repository] = {
    val repos = mutable.ListBuffer[Repository]()
    repos.addAll((root \ "distributionManagement" \ "repository").map(Repository(_, this)))
    repos.addAll((root \ "distributionManagement" \ "snapshotRepository").map(SnapshotRepository(_, this)))
    repos.toList
  }
  val modules: mutable.Set[Pom] = mutable.Set()
  (root \\ "module").foreach(module => {
    val modulePomFile = pomFile.map(p => Paths.get(p.getParent, module.text, "pom.xml").toFile)
    modulePomFile.foreach(mf => if (mf.exists()) modules.addOne(Pom(mf, m2RepoDirectory, Some(this))))
  })
  val properties: mutable.Map[String, PropertyValue] = mutable.TreeMap[String, PropertyValue]()
  properties
    .addAll((root \ "properties")
      .flatMap(_.child.filter(_.text.trim.nonEmpty)
        .map(pp => (pp.label, PropertyValue(pp.text.trim))))
    )

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

  val rootline: Seq[Pom] = rootLine()
  val consolidatedProperties: mutable.Map[String, PropertyValue] = {
    val props = mutable.Map[String, PropertyValue]()
    rootline.foreach(p =>{
      if (p.properties != null) {
        props.addAll(
          p.properties)
      }
    })
    props
  }
  setMavenProperties()

  var visited: Boolean = false

  override def toString: String = root.toString()

  def artifactPackaging(): String = {
    rootline.reverse.find(_.packaging.nonEmpty).map(_.packaging).getOrElse("")
  }

  def artifactName(): String = {
    rootline.reverse.find(_.artifact.name.nonEmpty).map(_.artifact.name).getOrElse("")
  }

  def artifactDescription(): String = {
    rootline.reverse.find(_.artifact.description.nonEmpty).map(_.artifact.description).getOrElse("")
  }

  def artifactId(): String = {
    rootline.reverse.find(_.artifact.artifactId.nonEmpty).map(_.artifact.artifactId).getOrElse("")
  }

  def artifactGroupId(): String = {
    rootline.reverse.find(_.artifact.groupId.nonEmpty).map(_.artifact.groupId).getOrElse("")
  }

  def artifactVersion(): String = {
    rootline.reverse.find(_.artifact.version.nonEmpty).map(_.artifact.version).getOrElse("")
  }

  def setMavenProperties(): Unit = {
    properties.addOne("project.packaging", PropertyValue(artifactPackaging()))
    properties.addOne("project.groupId", PropertyValue(artifactGroupId()))
    properties.addOne("project.artifactId", PropertyValue(artifactId()))
    properties.addOne("project.name", PropertyValue(artifactName()))
    properties.addOne("project.description", PropertyValue(artifactDescription()))
    properties.addOne("project.version", PropertyValue(artifactVersion()))

    val baseDir = pomFile.map(_.getParentFile.getCanonicalFile)
    baseDir.foreach(bd => {
      properties.addOne("basedir", PropertyValue(bd.getPath))
      properties.addOne("project.basedir", PropertyValue(bd.getPath))
      properties.addOne("project.baseUri", PropertyValue(bd.toURI.toString))
      properties.addOne("maven.multiModuleProjectDirectory", PropertyValue(bd.getPath))

      var buildDirectory = if (build.nonEmpty && build.get.directory.nonEmpty) Paths.get(bd.getPath, build.get.directory).toFile.getCanonicalPath else ""
      buildDirectory = if (buildDirectory.isEmpty) Paths.get(bd.getPath, "target").toFile.getCanonicalPath else buildDirectory
      properties.addOne("project.build.directory", PropertyValue(buildDirectory))

      var sourceDirectory = if (build.nonEmpty && build.get.sourceDirectory.nonEmpty) Paths.get(bd.getPath, build.get.sourceDirectory).toFile.getCanonicalPath else ""
      sourceDirectory = if (sourceDirectory.isEmpty) Paths.get(bd.getPath, "src", "main", "java").toFile.getCanonicalPath else sourceDirectory
      properties.addOne("project.build.sourceDirectory", PropertyValue(sourceDirectory))

      var outputDirectory = if (build.nonEmpty && build.get.outputDirectory.nonEmpty) Paths.get(bd.getPath, build.get.outputDirectory).toFile.getCanonicalPath else ""
      outputDirectory = if (outputDirectory.isEmpty) Paths.get(buildDirectory, "classes").toFile.getCanonicalPath else outputDirectory
      properties.addOne("project.build.outputDirectory", PropertyValue(outputDirectory))

      var testSourceDirectory = if (build.nonEmpty && build.get.testSourceDirectory.nonEmpty) Paths.get(bd.getPath, build.get.testSourceDirectory).toFile.getCanonicalPath else ""
      testSourceDirectory = if (testSourceDirectory.isEmpty) Paths.get(bd.getPath, "src", "test", "java").toFile.getCanonicalPath else testSourceDirectory
      properties.addOne("project.build.testSourceDirectory", PropertyValue(testSourceDirectory))

      var testOutputDirectory = if (build.nonEmpty && build.get.testOutputDirectory.nonEmpty) Paths.get(bd.getPath, build.get.testOutputDirectory).toFile.getCanonicalPath else ""
      testOutputDirectory = if (testOutputDirectory.isEmpty) Paths.get(buildDirectory, "test-classes").toFile.getCanonicalPath else testOutputDirectory
      properties.addOne("project.build.testOutputDirectory", PropertyValue(testOutputDirectory))
    })
  }

  def dfs(node: Pom = this, dfsSet: mutable.LinkedHashSet[Pom] = mutable.LinkedHashSet[Pom]()): mutable.LinkedHashSet[Pom] = {
    if (!node.visited) {
      node.visited = true
      node.childPoms.foreach(reference => dfs(reference, dfsSet))
      dfsSet.add(node)
    }
    dfsSet
  }

  def rootLine(poms: mutable.ListBuffer[Pom] = mutable.ListBuffer()): Seq[Pom] = {
    parentPom.foreach(_.rootLine(poms))
    poms.addOne(this)
    poms.toSeq
  }
}

object Pom {
  def apply(pomFile: File, m2RepoDirectory: String, parentPom: Option[Pom] = None, pomNode: Option[PomNode] = None) = new Pom(XML.loadFile(pomFile), m2RepoDirectory, Some(pomFile), parentPom, pomNode)

  def apply(pom: String, m2RepoDirectory: String, pomNode: Option[PomNode]) = new Pom(XML.loadString(pom), m2RepoDirectory, None, None, pomNode)

  def apply(pom: InputStream, m2RepoDirectory: String, pomNode: Option[PomNode]) = new Pom(XML.load(pom), m2RepoDirectory, None, None, pomNode)
}
