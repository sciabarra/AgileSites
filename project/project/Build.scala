import sbt._

object Plugins extends Build {
  lazy val root = Project("root", file(".")) dependsOn(
    (file("project") / "sbt-assembly-0.8.4").toURL.toURI
  )
}
