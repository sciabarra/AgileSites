package demo

import demo.view._

class Setup extends wcs.scala.SetupBase {

  def templates =
    Template("TestTemplateScala", classOf[TestElementScala].toString) ::
      // not sure why I cannot use classOf with a Java class
      Template("TestTemplateJava", "demo.view.TestElementJava") ::
      Nil

  def csElements =
    CSElement("TestCSElement", classOf[Tester].toString) ::
      Nil

  def siteEntries =
    SiteEntry("TestSiteEntry", classOf[Tester].toString) ::
      Nil
}

object Setup {
  def main(args: Array[String]) {
    println(new demo.Setup().exec("fwadmin", "xceladmin"))
  }
}