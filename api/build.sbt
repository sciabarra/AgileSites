val v = "1.9-M3"

def settingsByVersion(ver: String) = Seq(
    name := "agilesites2-api",
    organization := "com.sciabarra",
    version := ver + "_" + v,
    scalaVersion := "2.10.4",
    crossPaths := false,
    resolvers += Resolver.mavenLocal,
	unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "src" / "main" / "java",
    libraryDependencies ++= Seq(
      "com.sciabarra" % "agilesites2-core" % version.value % "provided",
      "junit" % "junit" % "4.11",
      "com.novocode" % "junit-interface" % "0.9" % "test",
      "log4j" % "log4j" % "1.2.16" % "provided",
      "com.oracle.sites" % "cs-core" % ver % "provided",
      "com.oracle.sites" % "cs" % ver % "provided",
      "com.oracle.sites" % "xcelerate" % ver % "provided",
      "com.oracle.sites" % "assetapi" % ver % "provided",
      "com.oracle.sites" % "assetapi-impl" % ver % "provided",
      "com.oracle.sites" % "jsoup" % ver % "provided",
      "com.oracle.sites" % "xstream" % ver % "provided") ++
      (if(ver.startsWith("11.")) Seq("com.oracle.sites" % "wem-sso-api" % ver % "provided") else Seq()))
 
val btsettings = bintrayPublishSettings ++ Seq(
	bintray.Keys.bintrayOrganization in bintray.Keys.bintray := Some("sciabarra"),
	bintray.Keys.repository in bintray.Keys.bintray := "maven",
	licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html")),
	publishMavenStyle := true,
	publishArtifact in packageDoc := false,
	publishArtifact in Test := false)

val api118 = project.in(file("api118")).settings(settingsByVersion("11.1.1.8.0"): _*).settings(btsettings: _*)

val api116 = project.in(file("api116")).settings(settingsByVersion("11.1.1.6.0"): _*).settings(btsettings: _*)

val api762 = project.in(file("api762")).settings(settingsByVersion("7.5.0"): _*).settings(btsettings: _*)

val api = project.in(file(".")).aggregate(api118,api116,api762).settings(sources in Compile := Seq())