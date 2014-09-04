package agilesites.build

import sbt._
import Keys._
import agilesites.build.util.Utils

trait TomcatSettings extends Utils {
  this: Plugin with ConfigSettings =>

  lazy val tomcatEmbeddedClasspath = taskKey[Seq[File]]("tomcat classpath")
  val tomcatEmbeddedClasspathTask = tomcatEmbeddedClasspath <<= (update) map {
    report => report.select(configurationFilter("tomcat"))
  }

  def tomcatEmbedded(base: File, port: Int, classpath: Seq[File], webapps: Seq[String], debug: Boolean) = {

    import java.io._
    import java.io.File.pathSeparator

    val classpathExt = classpath ++ Seq(file("bin"), file("bin") / "setup.jar", file("home") / "bin")

    val cp = classpathExt.map(_.getAbsolutePath).mkString(pathSeparator)
    val temp = base / "temp"
    val bin = base / "bin"
    temp.mkdirs

    val debugSeq = if (debug)
      "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000" :: Nil
    else Nil

    val opts = "-cp" :: cp ::
      "-Djava.io.tmpdir=" + (temp.getAbsolutePath) ::
      "-Xms256m" :: "-Xmx1024m" :: "-XX:MaxPermSize=256m" ::
      s"-Dorg.owasp.esapi.resources=${bin}" :: debugSeq

    val args = Seq(port.toString, base.getAbsolutePath) ++ webapps

    val cmd = opts ++ args

    val forkOpt = ForkOptions(
      runJVMOptions = opts,
      envVars = Map("CATALINA_HOME" -> base.getAbsolutePath),
      workingDirectory = Some(base))
    Fork.java(forkOpt, "setup.SitesServer" +: args)
  }

  def tomcatEmbeddedScript(base: File, port: Int, classpath: Seq[File], webapps: Seq[String]) = {
    if (java.io.File.pathSeparator == ";") {
      val basepath = base.getAbsolutePath
      val classpathCmd = classpath.map(x => "set CP=%CP%;\"" + x.getAbsolutePath + "\"").mkString("\r\n")
      val classpathSvc = classpath.map(x => "\"" + x.getAbsolutePath + "\"").mkString(";")
      
      val fw = new java.io.FileWriter("sites-server.cmd")
      val javahome = System.getProperty("java.home")
      val apps = webapps.mkString("\"", "\" \"", "\"")
      val apps2 = webapps.mkString("\"", "\";\"", "\"")
      val startArgs = s"""${port} "${basepath}"  ${apps}"""
      val startArgs2 = s"""${port};"${basepath}";${apps2}"""
      val port1 = (port + 1).toString
      val opts = """-Djava.io.tmpdir="${basepath}\temp" -Xms256m -Xmx1024m -XX:MaxPermSize=256m -Dorg.owasp.esapi.resources="${basepath}\bin""""
      val opts2 = opts.replace(' ', ';')

      
      fw.write(s"""@echo off
set CATALINA_HOME="${basepath}"
set JAVA="${javahome}\\bin\\java.exe"
set PRUN="${basepath}\\bin\\sites.exe"
set CP="${basepath}\\bin";"${basepath}\\bin\\setup.jar";"${basepath}\\home\\bin"
${classpathCmd}
set OPTS=${opts}
if "%~1"=="debug" set OPTS=%OPTS% -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000
if "%~1"=="stop" goto stop
if "%~1"=="start" goto start
if "%~1"=="install" goto install
if "%~1"=="uninstall" goto uninstall
if "%~1"=="run" goto run
if "%~1"=="debug" goto run
if "%~1"=="kill" goto kill
echo "usage: start|stop|install|uninstall|run|debug|kill"
goto end
:run
%JAVA% -cp %CP% %OPTS% setup.SitesServer ${startArgs}
goto end
:install
%PRUN% //IS//sites --DisplayName="Sites" --Install="sites.exe" --Startup=auto --JavaHome="${javahome}" --Classpath=${classpathSvc} --StartClass=setup.SitesServer --StartParams=${startArgs2} --StopClass=setup.SitesServer ++StopParams="stop;${port1}" --StartMode=java ++JvmOptions="${opts2}"  --LogPath="${base}\\logs" --StdOutput --StdError
goto end
:uninstall
%PRUN% //DS//sites
goto end
:start
%PRUN% //ES//sites
goto end
:stop
%PRUN% //SS//sites
goto end
:kill
%JAVA% -cp %CP% %OPTS% setup.SitesServer stop ${port + 1}
:end
""")
      fw.close
    }
  }

  lazy val sitesServer = inputKey[Unit]("Launch Local Sites")

  lazy val sitesServerTask = sitesServer := {

    val args: Seq[String] = Def.spaceDelimited("<arg>").parsed
    val classpath = tomcatEmbeddedClasspath.value
    val port = sitesPort.value.toInt
    val base = baseDirectory.value
    val home = sitesHome.value
    val url = sitesUrl.value
    val log = streams.value.log
    val cs = (base / "webapps" / "cs").getAbsolutePath
    val cas = (base / "webapps" / "cas").getAbsolutePath

    //for(folder <- folders) println("*** -"+folder)  
    //val root = (base / "app" / "src" / "main" / "static").getAbsolutePath
    //val test = (base / "app" / "src" / "test" / "static").getAbsolutePath

    val webapps = Seq( /*"=" + root, "test=" + test, */ "cs=" + cs, "cas=" + cas)
    val usage = "usage: start [debug]|stop|status|script"

    args.headOption match {
      case None => println(usage)

      case Some("status") =>
        try {
          new java.net.ServerSocket(port + 1).close
          println("Local Sites Server not running")
        } catch {
          case e: Throwable =>
            //e.printStackTrace
            println("Local Sites Server running")
        }

      case Some("stop") =>
        try {
          println("*** stopping Local Sites Server ***")
          def sock = new java.net.Socket("127.0.0.1", port + 1)
          sock.getInputStream.read
          sock.close
          println("*** stopped Local Server Sites ***")
        } catch {
          case e: Throwable =>
            // e.printStackTrace
            println("Local Sites Server not running")
        }

      case Some("start") =>

        // start tomcat
        val debug = args.size == 2 && args(1) == "debug"

        val tomcat = new Thread() {
          override def run() {
            try {
              println(s"*** Local Sites Server starting in port ${port}***")
              val tomcatProcess = tomcatEmbedded(base, port, classpath, webapps, debug)
            } catch {
              case e: Throwable =>
                //e.printStackTrace
                println("!!! Local Sites Server already running")
            }
          }
        }
        tomcat.start
        Thread.sleep(3000);
        println(" *** Waiting for Local Sites Server startup to complete ***")
        println(httpCallRaw(url + "/HelloCS"))

      case Some("script") =>
        tomcatEmbeddedScript(base, port, classpath, webapps)

      case Some(thing) =>
        println(usage)
    }
  }

  //val tomcatConfig = "tomcat"
  val tomcatVersion = "7.0.52"
  val hsqlVersion = "1.8.0.10"
  def tomcatDeps(tomcatConfig: String) = Seq(
    //"org.apache.tomcat" % "tomcat-catalina" % tomcatVersion % tomcatConfig,
    "org.apache.tomcat" % "tomcat-dbcp" % tomcatVersion % tomcatConfig,
    "org.apache.tomcat.embed" % "tomcat-embed-logging-log4j" % tomcatVersion % tomcatConfig,
    "org.apache.tomcat.embed" % "tomcat-embed-core" % tomcatVersion % tomcatConfig,
    "org.apache.tomcat.embed" % "tomcat-embed-jasper" % tomcatVersion % tomcatConfig,
    "org.hsqldb" % "hsqldb" % hsqlVersion % tomcatConfig)

  val tomcatSettings = Seq(ivyConfigurations += config("tomcat"),
    libraryDependencies ++= tomcatDeps("tomcat") ++ tomcatDeps("provided"),
    tomcatEmbeddedClasspathTask, sitesServerTask)
    
}