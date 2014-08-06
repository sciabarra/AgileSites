name := "agilesites2-core"

organization := "com.sciabarra"

version := "1.9_11.1.1.8.0"

scalaVersion := "2.10.4"

resolvers += Resolver.mavenLocal

unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "src" / "main" / "java"

publishArtifact in packageDoc := false

crossPaths := false

libraryDependencies ++= Seq(
	"log4j" % "log4j" % "1.2.16",
	"org.xeustechnologies" % "jcl-core" % "2.2.1", 
	"com.oracle.sites" % "cs-core" % "11.1.1.8.0",
	"com.oracle.sites" % "cs" % "11.1.1.8.0",
	"com.oracle.sites" % "xcelerate" % "11.1.1.8.0",
	"com.oracle.sites" % "assetapi" % "11.1.1.8.0",
	"com.oracle.sites" % "assetapi-impl" % "11.1.1.8.0",
	"com.oracle.sites" % "wem-sso-api" % "11.1.1.8.0")
