package agilesites.build

import sbt._
import Keys._

import agilesites.build.util.ScpTo
import agilesites.build.util.Utils

trait DeploySettings {
  this: Plugin with ConfigSettings with UtilSettings with Utils =>

  // package jar task - build the jar and copy it  to destination 
  lazy val asPackage = taskKey[Unit]("AgileSites package jar")
  val asPackageTask = asPackage := {
    val jar = (Keys.`package` in Compile).value
    val log = streams.value.log
    asUploadTarget.value match {
      case Some(url) =>
        val targetUri = new java.net.URI(url)
        val Array(user, pass) = targetUri.getUserInfo.split(":")
        val host = targetUri.getHost
        val path = targetUri.getPath
        val port = if (targetUri.getPort == -1) 22 else targetUri.getPort
        if (!ScpTo.scp(jar.getAbsolutePath(), user, pass, host, port, path))
          log.error("!!! cannot upload ")
        else
          log.info("uploaded " + url)
      case None =>
        val destdir = file(sitesShared.value) / "agilesites"
        val destjar = file(sitesShared.value) / "agilesites" / jar.getName

        destdir.mkdir
        IO.copyFile(jar, destjar)
        log.info("+++ " + destjar.getAbsolutePath)
        destjar.getAbsolutePath.toString
    }
  }

  /**
   * Extract the index of the classes annotated with the @Index annotation
   */
  def extractClassAndIndex(file: File): Option[Tuple2[String, String]] = {
    import scala.io._

    //println("***" + file)

    var packageRes: Option[String] = None;
    var indexRes: Option[String] = None;
    var classRes: Option[String] = None;
    val packageRe = """.*package\s+([\w\.]+)\s*;.*""".r;
    val indexRe = """.*@Index\(\"(.*?)\"\).*""".r;
    val classRe = """.*class\s+(\w+).*""".r;

    if (file.getName.endsWith(".java") || file.getName.endsWith(".scala"))
      for (line <- Source.fromFile(file).getLines) {
        line match {
          case packageRe(m) =>
            //println(line + ":" + m)
            packageRes = Some(m)
          case indexRe(m) =>
            //println(line + ":" + m)
            indexRes = Some(m)
          case classRe(m) =>
            //println(line + ":" + m)
            classRes = Some(m)
          case _ => ()
        }
      }

    if (packageRes.isEmpty || indexRes.isEmpty || classRes.isEmpty)
      None
    else {
      val t = (indexRes.get, packageRes.get + "." + classRes.get)
      Some(t)
    }
  }

  // generate index classes from sources
  val generateIndexTask = Def.task {
    val (analysis, dstDir, s) =
      ((compile in Compile).value, (resourceManaged in Compile).value, streams.value)

    val groupIndexed =
      analysis.apis.allInternalSources. // all the sources
        map(extractClassAndIndex(_)). // list of Some(index, class) or Nome
        flatMap(x => x). // remove None
        groupBy(_._1). // group by (index, (index, List(class)) 
        map { x => (x._1, x._2 map (_._2)) }; // lift to (index, List(class))

    //println(groupIndexed)

    val l = for ((subfile, lines) <- groupIndexed) yield {
      val file = dstDir / subfile
      val body = lines mkString ("# generated - do not edit\n", "\n", "\n# by AgileSites build\n")
      writeFile(file, body, s.log)
      file
    }
    l.toSeq
  }

  // copy resources to the webapp task
  lazy val asDeploy = taskKey[Unit]("Sites deploy")
  val asDeployTask = asDeploy := {
    val log = streams.value.log
    val url = sitesUrl.value
    if (sitesHello.value.isEmpty) {
      log.error(s"Sites must be up and running as s{url}.")
    } else {
      asPackage.value
      log.info(httpCall("Setup", "&sites=%s".format(asSites.value), url, sitesUser.value, sitesPassword.value))
    }
  }

  val deploySettings = Seq(asPackageTask, asDeployTask,
    (resourceGenerators in Compile) ++= Seq(generateIndexTask.taskValue))

}