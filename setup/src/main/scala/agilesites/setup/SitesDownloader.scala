package agilesites.setup

import scalafx.scene.web.WebView

class SitesDownloader extends WebView {

  val defaultURL = "http://www.oracle.com/technetwork/middleware/webcenter/sites/downloads/index.html"

  engine.load(defaultURL)
  engine.location.onChange((_, _, newValue) => println(newValue))

}