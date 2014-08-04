package agilesites.setup

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.Region
import scalafx.scene.layout.HBox
import scalafx.scene.control.Button
import scalafx.scene.layout.Priority
import scalafxml.core.FXMLView
import scalafxml.core.DependenciesByType
import scala.reflect.runtime.universe.typeOf

object Setup extends JFXApp {

  import JFXApp._

  val welcomeDeps = new DependenciesByType(Map())
  val welcome = FXMLView(getClass.getResource("welcome.fxml"), welcomeDeps)

  val download = new SitesDownloader
  
  val rootDeps = new DependenciesByType(
    Map(typeOf[SetupData] -> new SetupData(Seq(
        "Welcome to AgileSites Installer" -> welcome,
        "Please download WebCenterSites" -> download
      ))))
    
  val root = FXMLView(getClass.getResource("toplevel.fxml"), rootDeps)

  stage = new PrimaryStage {
    title = "AgileSites Installer"
    resizable = false
    scene = new Scene(root)
  }

}