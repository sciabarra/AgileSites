import scala.xml._

object AttrTypes {

  val f = (XML.load("""A:\Dropbox\Products\sciabarra\AgileSites\1.1\export\stargaze\demo\AttrTypes\1351275812122.xml""") \
   "asset" \ "attribute" apply (9)) \ "file" apply (0)
                                                  //> f  : scala.xml.Node = <file name="314\209/1351275793612.txt">
                                                  //| PD9YTUwgVkVSU0lPTj0iMS4wIj8+DQo8UFJFU0VOVEFUSU9OT0JKRUNUIE5BTUU9ImNrZWRpdG9y
                                                  //| Ij4gDQo8Q0tFRElUT1IgV0lEVEg9IjUwMHB4IiBIRUlHSFQ9IjMwMHB4IiAvPg0KPC9QUkVTRU5U
                                                  //| QVRJT05PQkpFQ1Q+</file>
      
  import wcs.build._
  
	val x = AssetCompiler.fileValue("x", f)   //> x  : String = "		data.getAttributeData("x").setData(blob("314\\\\
                                                  //| 209/1351275793612.txt",
                                                  //| "<?XML VERSION=\\\"1.0\\\"?>\\r\\n"+
                                                  //| "<PRESENTATIONOBJECT NAME=\\\"ckeditor\\\"> \\r\\n"+
                                                  //| "<CKEDITOR WIDTH=\\\"500px\\\" HEIGHT=\\\"300px\\\" />\\r\\n"+
                                                  //| "</PRESENTATIONOBJECT>"));"

	val s = AssetCompiler.escape("a\"b\nc")   //> s  : String = a\"b\nc

  "%s%".replace("%s%", x)                         //> res0: String = "		data.getAttributeData("x").setData(blob("314\\\\
                                                  //| 209/1351275793612.txt",
                                                  //| "<?XML VERSION=\\\"1.0\\\"?>\\r\\n"+
                                                  //| "<PRESENTATIONOBJECT NAME=\\\"ckeditor\\\"> \\r\\n"+
                                                  //| "<CKEDITOR WIDTH=\\\"500px\\\" HEIGHT=\\\"300px\\\" />\\r\\n"+
                                                  //| "</PRESENTATIONOBJECT>"));"

}