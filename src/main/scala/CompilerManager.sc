import wcs.build._
//.
object CompilerManager {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  val acm = new AssetCompilerManager("Demo", new java.io.File("A:\\Dropbox\\Products\\sciabarra\\AgileSites\\1.1"))
                                                  //> acm  : wcs.build.AssetCompilerManager = wcs.build.AssetCompilerManager@6d820
                                                  //| beb
	acm.compileAll                            //> res0: Array[String] = Array(A:\Dropbox\Products\sciabarra\AgileSites\1.1\app
                                                  //| \src\main\java\demo\content\attrtypes\AttrTypes1351275802523.java, A:\Dropbo
                                                  //| x\Products\sciabarra\AgileSites\1.1\app\src\main\java\demo\content\attrtypes
                                                  //| \AttrTypes1351275802532.java, A:\Dropbox\Products\sciabarra\AgileSites\1.1\a
                                                  //| pp\src\main\java\demo\content\attrtypes\AttrTypes1351275812113.java, A:\Drop
                                                  //| box\Products\sciabarra\AgileSites\1.1\app\src\main\java\demo\content\attrtyp
                                                  //| es\AttrTypes1351275812122.java, A:\Dropbox\Products\sciabarra\AgileSites\1.1
                                                  //| \app\src\main\java\demo\content\attrtypes\AttrTypes1351275814456.java, A:\Dr
                                                  //| opbox\Products\sciabarra\AgileSites\1.1\app\src\main\java\demo\content\attrt
                                                  //| ypes\AttrTypes1351275814471.java, A:\Dropbox\Products\sciabarra\AgileSites\1
                                                  //| .1\app\src\main\java\demo\content\attrtypes\AttrTypes1351275814585.java, A:\
                                                  //| Dropbox\Products\sciabarra\AgileSites\1.1\app\src\main\java\demo\content\att
                                                  //| rtypes\AttrTypes13512758
                                                  //| Output exceeds cutoff limit.
}