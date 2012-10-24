package demo

import demo.view._

class Model extends wcs.scala.Setup {

  val n = 10000000;

  def site = Site(n, "Test", "Test Site",
    Array("CEElement", "Templates", "SiteEntry"),
    Array("fwadmin"),
    Array("AdvancedUser"))

  def templates =
    Template(n, "TestTemplateScala", "TestTemplateScala", classOf[TestElementScala].toString) ::
      // not sure why I cannot use classOf with a Java class
      Template(n + 1, "TestTemplateJava", "Test Template Java", "demo.view.TestElementJava") ::
      Nil

  def csElements =
    CSElement(n + 2, "TestCSElement", "Test CSElement", classOf[Tester].toString) ::
      Nil

  def siteEntries =
    SiteEntry(n + 3, "TestSiteEntry", "Test CSElement", classOf[Tester].toString) ::
      Nil
}

object Model {
  def main(args: Array[String]) {
    println(new demo.Model().exec("fwadmin", "xceladmin"))
  }
}