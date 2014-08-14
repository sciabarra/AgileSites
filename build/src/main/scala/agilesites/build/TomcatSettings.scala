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

  def tomcatEmbedded(base: File, port: Int, classpath: Seq[File], webapps: Seq[String], debug: Boolean, onlyCreateScript: Boolean = false) = {

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

    if (onlyCreateScript) {
      def sel(x: String, y: String) = if (File.pathSeparator == ";") x else y
      val script = "sites-server" + (if (debug) "-debug." else ".") + sel("bat", "sh")
      val fw = new FileWriter(script)
      fw.write(sel("set ", "") + "CATALINA_HOME=\"" + base.getAbsolutePath + "\"" + sel("\r\n", "\n"))
      fw.write(cmd.mkString("java ", " ", sel("\r\n", "\n")))
      fw.close
      println("+++ " + script)
    } else {
      val forkOpt = ForkOptions(
        runJVMOptions = opts,
        envVars = Map("CATALINA_HOME" -> base.getAbsolutePath),
        workingDirectory = Some(base))
      Fork.java(forkOpt, "setup.SitesServer" +: args)
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
        tomcatEmbedded(base, port, classpath, webapps, false, true)
        tomcatEmbedded(base, port, classpath, webapps, true, true)

      case Some(thing) =>
        println(usage)
    }
  }

  //val tomcatConfig = "tomcat"
  val tomcatVersion = "7.0.52"
  val hsqlVersion = "1.8.0.10"
  def tomcatDeps(tomcatConfig: String) = Seq(
    "org.apache.tomcat" % "tomcat-catalina" % tomcatVersion % tomcatConfig,
    "org.apache.tomcat" % "tomcat-dbcp" % tomcatVersion % tomcatConfig,
    "org.apache.tomcat.embed" % "tomcat-embed-logging-log4j" % tomcatVersion % tomcatConfig,
    "org.apache.tomcat.embed" % "tomcat-embed-core" % tomcatVersion % tomcatConfig,
    "org.apache.tomcat.embed" % "tomcat-embed-core" % tomcatVersion % tomcatConfig,
    "org.apache.tomcat.embed" % "tomcat-embed-jasper" % tomcatVersion % tomcatConfig,
    "org.hsqldb" % "hsqldb" % hsqlVersion % tomcatConfig)

  val tomcatSettings = Seq(ivyConfigurations += config("tomcat"),
    libraryDependencies ++= tomcatDeps("tomcat") ++ tomcatDeps("compile"),
    tomcatEmbeddedClasspathTask, sitesServerTask)

}