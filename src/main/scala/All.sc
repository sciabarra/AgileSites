import wcs.build._
import scala.xml._

///

object All {


  val acm = new AssetCompilerManager("Demo", new java.io.File("A:\\Dropbox\\Products\\sciabarra\\AgileSites\\1.1"))
                                                  //> acm  : wcs.build.AssetCompilerManager = wcs.build.AssetCompilerManager@10caf
                                                  //| cf2

  acm.compile("AttrTypes", 1351275812122l)        //> res0: String = A:\Dropbox\Products\sciabarra\AgileSites\1.1\app\src\main\jav
                                                  //| a\demo\content\attrtypes\AttrTypes1351275812122.java
    
  acm.compile("PageAttribute", 1351275812112l)    //> res1: String = A:\Dropbox\Products\sciabarra\AgileSites\1.1\app\src\main\jav
                                                  //| a\demo\content\pageattribute\PageAttribute1351275812112.java
   
 
  acm.compile("PageDefinition", 1351275812156l)   //> res2: String = A:\Dropbox\Products\sciabarra\AgileSites\1.1\app\src\main\jav
                                                  //| a\demo\content\pagedefinition\PageDefinition1351275812156.java
  acm.compile("Page", 1351275812113l)             //> res3: String = A:\Dropbox\Products\sciabarra\AgileSites\1.1\app\src\main\jav
                                                  //| a\demo\content\page\Page1351275812113.java

}