def settingsByVersion(ver: String) = Seq(
    name := "agilesites2-api",
    organization := "com.sciabarra",
    version := "1.9_" + ver,
    scalaVersion := "2.10.4",
    resolvers += Resolver.mavenLocal,
    publishArtifact in packageDoc := false,
    crossPaths := false, 
    libraryDependencies ++= Seq(
      "log4j" % "log4j" % "1.2.16",
      "junit" % "junit" % "4.11",
      "com.sciabarra" % "agilesites2-core" % ("1.9_" + ver),
      "com.oracle.sites" % "cs-core" % ver,
      "com.oracle.sites" % "jsoup" % ver,	
      "com.oracle.sites" % "xstream" % ver,	
      "com.novocode" % "junit-interface" % "0.9" % "test"))
      
val api118 = project.in(file("api118")).settings(settingsByVersion("11.1.1.8.0"): _*)
  .settings(unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "src" / "main" / "java")

val api116 = project.in(file("api116")).settings(settingsByVersion("11.1.1.6.0"): _*)
  .settings(unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "src" / "main" / "java")

val api762 = project.in(file("api762")).settings(settingsByVersion("7.5.0"): _*)
  .settings(unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "src" / "main" / "java")
