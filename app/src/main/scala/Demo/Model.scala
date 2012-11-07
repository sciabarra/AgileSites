package demo

import demo.view._

class Model extends wcs.scala.Setup {

  val n = 10000000;

  def site = Site(n, "Test", "Test Site",
    Array("CSElement", "Template", "SiteEntry"),
    Array("fwadmin"),
    Array("AdvancedUser"))

  def assets: List[Asset] =
    CSElement(n + 1, "TestCSElement", "Test CSElement", classOf[Tester].getCanonicalName()) ::
      Template(n + 2, "TestTemplateScala", "TestTemplate", classOf[TestElementScala].getCanonicalName()) ::
      SiteEntry(n + 3, "TestSiteEntry", "Test CSElement", classOf[Tester].toString, true, n + 1) ::
      Nil

  // not sure why I cannot use classOf with a Java class
  //  Template(n + 1, "TestTemplateJava", "Test Template Java", "demo.view.TestElementJava") ::

}

object Model {
  def main(args: Array[String]) {
    println(new demo.Model().exec("fwadmin", "xceladmin"))
  }
}
