val v = "1.9-M2"

def settingsByVersion(ver: String) = bintrayPublishSettings ++ Seq(
  name := "agilesites2-core",
  organization := "com.sciabarra",
  version := ver + "_"+v,
  scalaVersion := "2.10.4",
  resolvers += Resolver.mavenLocal,
  publishArtifact in packageDoc := false,
  crossPaths := false,
  bintray.Keys.bintrayOrganization in bintray.Keys.bintray := Some("sciabarra"),
  bintray.Keys.repository in bintray.Keys.bintray := "maven",
  publishMavenStyle := true,
  unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "src" / "main" / "java",
  unmanagedResourceDirectories in Compile += baseDirectory.value.getParentFile / "src" / "main" / "resources",
  libraryDependencies ++= Seq(
  	   "com.novocode" % "junit-interface" % "0.9" % "test",
 	   "org.xeustechnologies" % "jcl-core" % "2.2.1", 
       "log4j" % "log4j" % "1.2.16" % "provided",
       "com.oracle.sites" % "cs-core" % ver % "provided",
       "com.oracle.sites" % "cs" % ver % "provided",
       "com.oracle.sites" % "xcelerate" % ver % "provided",
       "com.oracle.sites" % "assetapi" % ver % "provided",
       "com.oracle.sites" % "assetapi-impl" % ver % "provided") ++
       (if(ver.startsWith("11.")) Seq("com.oracle.sites" % "wem-sso-api" % ver % "provided") else Seq()))

val btsettings = bintrayPublishSettings ++ Seq(
	bintray.Keys.bintrayOrganization in bintray.Keys.bintray := Some("sciabarra"),
	bintray.Keys.repository in bintray.Keys.bintray := "maven",
	licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html")),
	publishMavenStyle := true,
	publishArtifact in packageDoc := false,
	publishArtifact in Test := false)

val core118 = project.in(file("core118")).settings(settingsByVersion("11.1.1.8.0"): _*).settings(btsettings: _*)

val core116 = project.in(file("core116")).settings(settingsByVersion("11.1.1.6.0"): _*).settings(btsettings: _*)

val core762 = project.in(file("core762")).settings(settingsByVersion("7.5.0"): _*).settings(btsettings: _*)

val core = project.in(file(".")).aggregate(core118, core116, core762).
           settings(sources in Compile := Seq(),
           		    libraryDependencies ++= Seq("log4j" % "log4j" % "1.2.16" % "provided", 
                                                "org.xeustechnologies" % "jcl-core" % "2.2.1"))