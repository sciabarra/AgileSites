package agilesites.setup

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.Region
import scalafx.scene.layout.HBox
import scalafx.scene.control.Button
import scalafx.scene.layout.Priority

object Hello extends JFXApp {

  import JFXApp._
  stage = new PrimaryStage {
    title = "AgileSites Installer"
    resizable = false
    scene = new Scene(640, 480) {
      root = new BorderPane {
        top = createTitle
        //center = createBody
        bottom = createNavigation
      }
    }
  }

  def createTitle = new Region {
    style = "-fx-background-color: radial-gradient(radius 100%, white, gray)"
  }

  def createNavigation = new HBox {
    //layoutX = 60
    //layoutY = 420
    spacing = 20
    style = "-fx-background-color: #336699;"
    content = List(
      new Button {
        text = "<< Prev"
        //onAction = handle { anim.playFromStart() }
        //disable <== anim.status =!= Status.STOPPED
      },
      new HBox {
        hgrow = Priority.ALWAYS
      },
      new Button {
        text = "Next >>"
        //onAction = handle { anim.pause() }
        //disable <== anim.status =!= Status.RUNNING
        
      })
  }

}