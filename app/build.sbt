name := "agilesites2-api"

organization := "com.sciabarra"

version := "1.9"

scalaVersion := "2.10.4"

resolvers += Resolver.mavenLocal

publishArtifact in packageDoc := false

crossPaths := false

libraryDependencies ++= Seq(
   "com.novocode" % "junit-interface" % "0.9" % "test",
   "log4j" % "log4j" % "1.2.16",
   "org.xeustechnologies" % "jcl-core" % "2.2.1", 
   "com.oracle.sites" % "cs-core" % "7.5.0",
   "com.oracle.sites" % "cs" % "7.5.0",
   "com.oracle.sites" % "xcelerate" % "7.5.0",
   "com.oracle.sites" % "assetapi" % "7.5.0",
   "com.oracle.sites" % "assetapi-impl" % "7.5.0")

