package demo

import demo.view._

class Model extends wcs.scala.Setup {

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

object Model {
  def main(args: Array[String]) {
    println(new demo.Model().exec("fwadmin", "xceladmin"))
  }
}