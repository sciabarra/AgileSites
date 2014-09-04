package agilesites.build

import sbt._
import Keys._
import agilesites.build.util.TagWrapperGenerator
import agilesites.build.util.Utils
import agilesites.build.util.ScpTo
import java.io.File

trait ToolsSettings {
  this: Plugin with UtilSettings with ConfigSettings =>

  // find the default workspace from sites
  def defaultWorkspace(sites: String) = normalizeSiteName(sites.split(",").head)

  def catalogManager(url: String, user: String, pass: String, jars: Seq[File], opts: Seq[String], log: Logger) {
    val cp = (Seq(file("bin").getAbsoluteFile) ++ jars).mkString(java.io.File.pathSeparator)

    val cmd = Seq("-cp", cp)
    val stdopts = Seq("-u", user, "-p", pass, "-b", url + "/CatalogManager")
    val all = if (opts.size > 0) {
      if (opts(0) == "view") cmd
      else cmd ++ stdopts ++ opts
    } else {
      log.info("use 'view' to display the gui")
      log.info("use a sequence of options for other functions")
      log.info("(not needed -u -p -b)")
      cmd ++ Seq("-h")
    }

    Fork.java(None, all, Some(new java.io.File(".")), log)

    /*
    val forkOpt = ForkOptions(
      runJVMOptions = all Seq("-cp", all.mkString(File.pathSeparator)),
      workingDirectory = Some(baseDirectory.value))

    Fork.java(forkOpt, "org.jbake.launcher.Main" +: args)
     */

  }

  lazy val sitesTagWrapperGen = inputKey[Unit]("Generate Tag Wrappers")
  lazy val sitesTagWrapperGenTask = sitesTagWrapperGen := {

    val args: Seq[String] = Def.spaceDelimited("<arg>").parsed
    if (args.size == 0)
      println("usage: siteTagWrapperGen <sites-webapp-folder>")
    else {
      val tldDir = file(args.head) / "WEB-INF" / "futuretense_cs"
      if (!tldDir.isDirectory)
        println("no tld founds in " + tldDir)
      else {
        val dstDir = (javaSource in Compile).value / "wcs" / "core" / "tags"
        for {
          tld <- tldDir.listFiles
          if tld.getName.endsWith(".tld")
        } yield {
          val src = tld.getAbsolutePath
          val clsj = TagWrapperGenerator.tld2class(src)
          val dstj = file(dstDir / clsj + ".java")
          val bodyj = TagWrapperGenerator(src)
          IO.write(dstj, bodyj)
          println("+++ " + dstj)
          dstj
        }
      }
    }
  }

  lazy val sitesCatalogManager = inputKey[Unit]("WCS Catalog Manager")
  val sitesCatalogManagerTask = sitesCatalogManager := {
    val args = Def.spaceDelimited("<arg>").parsed
    catalogManager(sitesUrl.value, sitesUser.value, sitesPassword.value, sitesPopulateClasspath.value, args, streams.value.log)
  }

  lazy val sitesPopulateClasspath = taskKey[Seq[File]]("Sites Populate Classpath")
  lazy val sitesPopulateClasspathTask = sitesPopulateClasspath <<= (sitesHome, baseDirectory) map {
    (home, base) =>
      val h = file(home)
      Seq(base / "bin", h / "bin") ++
        (h * "*.jar").get ++
        (h / "Sun" ** "*.jar").get ++
        (h / "wem" ** "*.jar").get
  }

  lazy val sitesPopulate = TaskKey[Unit]("Sites Catalog Manager Populate")
  val sitesPopulateTask = sitesPopulate <<=
    (sitesHello, sitesHome, sitesUrl, sitesUser, sitesPassword, sitesPopulateClasspath, streams) map {
      (hello, home, url, user, pass, classpath, s) =>
        if (hello.isEmpty)
          throw new Exception("Web Center Sites must be online.")
        val dir = file("core") / "populate"
        catalogManager(url, user, pass, classpath, Seq("-x", "import_all", "-d", dir.getAbsolutePath), s.log)
        (file(home) / "populate.done").createNewFile
    }

  lazy val csdtClientHome = settingKey[File]("CSDT Client Home")
  lazy val csdtEnvision = settingKey[File]("CSDT Envision Folder")
  lazy val csdtClientClasspath = settingKey[Seq[File]]("CSDT Client Classpath")

  // interface to csdt from sbt
  lazy val csdt = inputKey[Unit]("Content Server Development Tool")
  val csdtTask = csdt := {

    val args: Seq[String] = Def.spaceDelimited("<arg>").parsed
    val home = sitesHome.value
    val version = sitesVersion.value
    val url = sitesUrl.value
    val user = sitesUser.value
    val password = sitesPassword.value
    val sites = asSites.value
    val seljars = csdtClientClasspath.value
    val log = streams.value.log
    val envision = csdtEnvision.value
    val defaultSite = sites.split(",").head

    val defaultWorkspace = envision / defaultSite
    if(!defaultWorkspace.exists())
      defaultWorkspace.mkdir
      
    val workspaces = envision.listFiles.filter(_.isDirectory).map(_.getName)
    val workspaceSearch = (s"#${defaultSite}#" +: args).reverse.filter(_.startsWith("#")).head.substring(1)

    val workspace = if (!workspaceSearch.endsWith("#"))
      workspaces.filter(_.indexOf(workspaceSearch) != -1)
    else workspaces.filter(_ == workspaceSearch.init)

    val fromSites = (("!" + defaultSite) +: args).reverse.filter(_.startsWith("!")).head.substring(1)
    val toSites = (("^" + fromSites) +: args).reverse.filter(_.startsWith("^")).head.substring(1)

    //println(workspaceSearch)
    //println(workspace.mkString(" "))

    if (args.size > 0 && args(0) == "raw") {
      Run.run("com.fatwire.csdt.client.main.CSDT",
        seljars, args.drop(1), streams.value.log)(runner.value)
    } else if (args.size == 0) {
      println("""usage: csdt <cmd> <selector> ... [#<workspace>[#]] [!<from-sites>] [^<to-sites>]
                       | <workspace> is a substring of available workspaces, use #workspace# for an exact match
                       |   default workspace is: %s
                       |   available workspaces are: %s
                       | <from-sites> and <to-sites> is a comma separated list of sites defined, 
                       |   <from-sites> defaults to <workspace>, 
                       |   <to-sites> defaults to <from-sites> 
                       | <cmd> is one of 'listcs', 'listds', 'import', 'export', 'mkws'
                       | <selector> check developer tool documentation for complete syntax
                       |    you can use <AssetType>[:<id>] or a special form,
                       |    the special form are
                       |      @SITE @ASSET_TYPE @ALL_ASSETS @STARTMENU @TREETAB
                       |  and also additional @ALL for all of them
                       |""".stripMargin.format(defaultSite, workspaces.mkString("'", "', '", "'")))
    } else if (workspace.size == 0)
      println("workspace " + workspaceSearch + " not found - create it with mkws <workspace>")
    else if (workspace.size > 1)
      println("workspace " + workspaceSearch + " is ambigous")
    else {

      def processArgs(args: Seq[String]) = {
        if (args.size == 0 || args.size == 1) {
          println("""please specify what you want to export or use @ALL to export all
                       | you can use <AssetType>[:<id>] or a special form,
                       | the special form are
                       |   @SITE @ASSET_TYPE @ALL_ASSETS @STARTMENU @TREETAB @ROLE
                       |  and also additional @ALL meaning  all of them""".stripMargin)
          Seq()
        } else if (args.size == 2 && args(1) == "@ALL") {
          Seq("@SITE", "@ASSET_TYPE", "@ALL_ASSETS", "@STARTMENU", "@TREETAB", "@ROLE")
        } else {
          args.drop(1)
        }
      }

      val args1 = args.filter(!_.startsWith("#")).filter(!_.startsWith("!")).filter(!_.startsWith("^"))
      val firstArg = if (args1.size > 0) args1(0) else "listcs"
      val resources = firstArg match {
        case "listcs" => processArgs(args1)
        case "listds" => processArgs(args1)
        case "import" => processArgs(args1)
        case "export" => processArgs(args1)
        case "mkws" =>
          if (args1.size == 1) {
            println("please specify workspace name")
          } else {
            val ws = envision / args1(1)
            if (ws.exists)
              println("nothing to do - workspace " + args1(1) + " exists")
            else {
              ws.mkdirs
              if (ws.exists)
                println(" workspace " + args1(1) + " created")
              else
                println("cannot create workspace " + args1(1))
            }
          }
          Seq()
        case _ =>
          println("Unknown command")
          Seq()
      }

      for (res <- resources) {
        val cmd = Array(url + "/ContentServer",
          "username=" + user,
          "password=" + password,
          "cmd=" + firstArg,
          "resources=" + res,
          "fromSites=" + fromSites,
          "toSites=" + toSites,
          "datastore=" + workspace.head)

        log.debug(seljars.mkString("\n"))
        //s.log.debug(cmd.mkString(" "))
        Run.run("com.fatwire.csdt.client.main.CSDT", seljars, cmd, log)(runner.value)
      }
    }
  }

  val toolsSettings = Seq(sitesTagWrapperGenTask, csdtTask,
    csdtClientHome := baseDirectory.value / "sites" / "home" / "csdt" / "csdt-client",
    csdtEnvision := baseDirectory.value / "sites" / "home" / "export" / "envision",
    csdtClientClasspath := (csdtClientHome.value ** "*.jar").get)

}