def settingsByVersion(ver: String) = Seq(
  name := "agilesites2-core",
  organization := "com.sciabarra",
  version := "1.9_"+ver,
  scalaVersion := "2.10.4",
  resolvers += Resolver.mavenLocal,
  publishArtifact in packageDoc := false,
  crossPaths := false,
  unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "src" / "main" / "java",
  libraryDependencies ++= Seq("log4j" % "log4j" % "1.2.16",
       "org.xeustechnologies" % "jcl-core" % "2.2.1", 
       "com.oracle.sites" % "cs-core" % ver,
       "com.oracle.sites" % "cs" % ver,
       "com.oracle.sites" % "xcelerate" % ver ,
       "com.oracle.sites" % "assetapi" % ver ,
       "com.oracle.sites" % "assetapi-impl" % ver) ++
       (if(ver.startsWith("11.")) Seq("com.oracle.sites" % "wem-sso-api" % ver) else Seq()))

val core118 = project.in(file("core118")).settings(settingsByVersion("11.1.1.8.0"): _*)

val core116 = project.in(file("core116")).settings(settingsByVersion("11.1.1.6.0"): _*)

val core762 = project.in(file("core762")).settings(settingsByVersion("7.5.0"): _*)



