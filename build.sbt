// home directory of WCS
wcsHome in ThisBuild := "../ContentServer/11.1.1.6.0/"

// webapp directory of WCS
wcsWebapp in ThisBuild := "../App_Server/apache-tomcat-6.0.32/webapps/cs/"

// location of the csdt-client jar
wcsCsdtJar in ThisBuild := "../ContentServer/11.1.1.6.0/csdt-client-1.2.jar"

//url to content server
wcsUrl in ThisBuild := "http://localhost:8380/cs/"

// site to import/export
wcsSite in ThisBuild := "ScalaWCS"

// user to import/export
wcsUser in ThisBuild := "fwadmin"

// password to import/export
wcsPassword in ThisBuild := "xceladmin"

// scalacOptions += "-deprecation"

// javaOptions += "-Dlogback-access.debug=true"
