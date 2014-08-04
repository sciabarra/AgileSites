package agilesites.setup

import scalafxml.core.macros.sfxml
import scalafx.scene.control.Button
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox
import scalafx.scene.layout.HBox
import scalafx.scene.Node

import scalafx.event.ActionEvent

class Boh {
  val b: Button = null
  
}

case class SetupData(val nodeByMessage: Seq[(String, Node)])

@sfxml
class SetupPresenter(
  private val mainHbox: HBox,
  private val prevButton: Button,
  private val nextButton: Button,
  private val messageLabel: Label,
  private val setupData: SetupData) {

  
  val nodes = setupData.nodeByMessage.map(_._2)
  val messages = setupData.nodeByMessage.map(_._1)
  var position = 0
  updateByPosition
 
  def updateByPosition {
    
    prevButton.disable = (position == 0)
    nextButton.disable = (position == nodes.size -1)
       
    mainHbox.content = nodes(position)
    messageLabel.text = messages(position)
  }
    
  
  def onNext(event: ActionEvent) { 
    position = position + 1
    updateByPosition
  }

  def onPrev(event: ActionEvent) {
    position = position - 1
    updateByPosition
  }
  
}