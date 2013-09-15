import scala.xml._

object AttrTypes {

  (XML.load("""A:\Dropbox\Products\sciabarra\AgileSites\1.1\export\stargaze\demo\Page\1351275812113.xml""")) \\ "asset" \\ "@subtype" text
                                                  //> res0: String = Content1

  //((XML.load("""A:\Dropbox\Products\sciabarra\AgileSites\1.1\export\stargaze\demo\Page\1351275812113.xml""")) \\ "asset").startsWith(arg0)

  (XML.load("""A:\Dropbox\Products\sciabarra\AgileSites\1.1\export\stargaze\demo\AttrTypes\1351275812122.xml""")) \\ "asset" \\ "@subtype" text
                                                  //> res1: String = ""

}