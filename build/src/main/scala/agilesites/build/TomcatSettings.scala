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

  def forkTomcatEmbedded(base: File, port: Int, classpath: Seq[File], webapps: Seq[String], debug: Boolean) = {

    import java.io.File.pathSeparator

    val cp = classpath.map(_.getAbsolutePath).mkString(pathSeparator)
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

    val args = Seq("agilesites.build.util.EmbeddedTomcat", port.toString, base.getAbsolutePath) ++ webapps
    val cmd = opts.toList ++ args.toList

    import java.io._

    def sel(x: String, y: String) = if (File.pathSeparator == ";") x else y
    val fw = new FileWriter("tomcat." + sel("bat", "sh"))
    fw.write(sel("set ", "") + "CATALINA_HOME=\"" + base.getAbsolutePath + "\"" + sel("\r\n", "\n"))
    fw.write(cmd.mkString("java ", " ", sel("\r\n", "\n")))
    fw.close

    Fork.java.fork(None, cmd,
      Some(base),
      Map("CATALINA_HOME" -> base.getAbsolutePath),
      true, StdoutOutput)

    // TODO
    //val forkOpt = ForkOptions(
    //  runJVMOptions = jvmOpts,
    //  workingDirectory = Some(baseDirectory.value))
    //Fork.java(forkOpt, "org.jbake.launcher.Main" +: args)

  }

  lazy val sitesEmbedded = inputKey[Unit]("Launch Embedded Sites")

  lazy val sitesEmbeddedTask = sitesEmbedded := {

    val args: Seq[String] = Def.spaceDelimited("<arg>").parsed
    val classpath = tomcatEmbeddedClasspath.value
    val port = sitesEmbeddedPort.value.toInt
    val base = baseDirectory.value
    val home = sitesHome.value
    val url = sitesUrl.value
    val log = streams.value.log

    args.headOption match {

      case None => println("usage: start [debug]|stop|status")

      case Some("status") =>

        try {
          new java.net.ServerSocket(port + 1).close
          println("tomcat not running")
        } catch {
          case e: Throwable =>
            //e.printStackTrace
            println("tomcat running")
        }

      case Some("stop") =>
        try {
          println("*** stopping tomcat ***")
          def sock = new java.net.Socket("127.0.0.1", port + 1)
          sock.getInputStream.read
          sock.close
          println("*** stopped tomcat ***")
        } catch {
          case e: Throwable =>
            // e.printStackTrace
            println("tomcat not running")
        }

      case Some("start") =>

        // start tomcat
        val debug = args.size == 2 && args(1) == "debug"

        val tomcat = new Thread() {
          override def run() {
            try {

              println("*** tomcat starting in port 8181 ***")

              //for(folder <- folders) println("*** -"+folder)  
              //val root = (base / "app" / "src" / "main" / "static").getAbsolutePath
              //val test = (base / "app" / "src" / "test" / "static").getAbsolutePath

              val cs = (base / "webapps" / "cs").getAbsolutePath
              val cas = (base / "webapps" / "cas").getAbsolutePath

              val webapps = Seq( /*"=" + root, "test=" + test, */ "cs=" + cs, "cas=" + cas)
              val tomcatProcess = forkTomcatEmbedded(base, port, classpath, webapps, debug)

            } catch {
              case e: Throwable =>
                //e.printStackTrace
                println("!!! tomcat already running")
            }
          }
        }
        tomcat.start
        Thread.sleep(3000);
        // wait for cs startup if deployed
        if ((file("wcs") / "webapps" / "cs").isDirectory) {
          println(" *** Waiting for CS startup complete ***")
          println(httpCallRaw(url + "/HelloCS"))
        }

      case Some(thing) =>
        println("usage: start [debug]|stop|status")
    }
  }
  

 val tomcatSettings = Seq(libraryDependencies ++= tomcatDeps,
    ivyConfigurations += config(tomcatConfig),
    tomcatEmbeddedClasspathTask, sitesEmbeddedTask)
    
    //val tomcatConfig = "tomcat"
val tomcatConfig = "compile"

val tomcatVersion = "7.0.52"

val hsqlVersion = "1.8.0.10"

val tomcatDeps = Seq(
    "org.apache.tomcat" % "tomcat-catalina" % tomcatVersion % tomcatConfig,
    "org.apache.tomcat" % "tomcat-dbcp" % tomcatVersion % tomcatConfig,
    "org.apache.tomcat.embed" % "tomcat-embed-logging-log4j" % tomcatVersion % tomcatConfig,
    "org.apache.tomcat.embed" % "tomcat-embed-core" % tomcatVersion % tomcatConfig,
    "org.apache.tomcat.embed" % "tomcat-embed-core" % tomcatVersion % tomcatConfig,
    "org.apache.tomcat.embed" % "tomcat-embed-jasper" % tomcatVersion % tomcatConfig,
    "org.hsqldb" % "hsqldb" % hsqlVersion % tomcatConfig)

}