import wcs.build._
import scala.xml._
import java.io.File

//...

object All {

	//new File(".").getAbsolutePath

	val base = new File("A:\\Dropbox\\Products\\sciabarra\\AgileSites\\1.1")
                                                  //> base  : java.io.File = A:\Dropbox\Products\sciabarra\AgileSites\1.1
	
	val xml = new File(base.getAbsolutePath +"\\src\\test\\java")
                                                  //> xml  : java.io.File = A:\Dropbox\Products\sciabarra\AgileSites\1.1\src\test\
                                                  //| java
	
	val java = new File(xml.getAbsolutePath+"\\demo\\content")
                                                  //> java  : java.io.File = A:\Dropbox\Products\sciabarra\AgileSites\1.1\src\test
                                                  //| \java\demo\content
	
  val acm = new AssetCompilerManager("Demo", base, Some(xml), Some(java))
                                                  //> acm  : wcs.build.AssetCompilerManager = wcs.build.AssetCompilerManager@278ef
                                                  //| c0c
  

  //acm.compile("AttrTypes", 1351275812113l)
    
  acm.compile("PageAttribute", 1351275812112l)    //> res0: String = A:\Dropbox\Products\sciabarra\AgileSites\1.1\src\test\java\de
                                                  //| mo\content\pageattribute\PageAttribute1351275812112.java
  acm.compile("PageDefinition", 1351275812156l)   //> res1: String = A:\Dropbox\Products\sciabarra\AgileSites\1.1\src\test\java\de
                                                  //| mo\content\pagedefinition\PageDefinition1351275812156.java
  acm.compile("Page", 1351275812113l)             //> res2: String = A:\Dropbox\Products\sciabarra\AgileSites\1.1\src\test\java\de
                                                  //| mo\content\page\Page1351275812113.java

 acm.compile("Kpn_Group", 1351275803269l)         //> res3: String = A:\Dropbox\Products\sciabarra\AgileSites\1.1\src\test\java\de
                                                  //| mo\content\kpngroup\Kpn_Group1351275803269.java
 

}