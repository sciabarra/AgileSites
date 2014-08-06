name := "agilesites2-core"

organization := "com.sciabarra"

version := "1.9_7.5.0"

scalaVersion := "2.10.4"

resolvers += Resolver.mavenLocal

unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "src" / "main" / "java"

publishArtifact in packageDoc := false

crossPaths := false

libraryDependencies ++= Seq(
   "log4j" % "log4j" % "1.2.16",
   "org.xeustechnologies" % "jcl-core" % "2.2.1", 
   "com.oracle.sites" % "cs-core" % "7.5.0",
   "com.oracle.sites" % "cs" % "7.5.0",
   "com.oracle.sites" % "xcelerate" % "7.5.0",
   "com.oracle.sites" % "assetapi" % "7.5.0",
   "com.oracle.sites" % "assetapi-impl" % "7.5.0")
