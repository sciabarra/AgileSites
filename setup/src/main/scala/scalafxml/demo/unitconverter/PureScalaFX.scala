package scalafxml.demo.unitconverter

import scalafx.application.{Platform, JFXApp}
import scalafx.Includes._
import scalafx.scene.Scene
import scalafx.scene.layout.{Priority, ColumnConstraints, GridPane}
import scalafx.scene.control.{Button, TextField, ComboBox, Label}
import scalafx.geometry.{Insets, HPos}
import scalafx.event.ActionEvent
import javafx.beans.binding.StringBinding

class PureScalaFXView(converters: UnitConverters) extends JFXApp.PrimaryStage {

  // UI Definition
  title = "Unit conversion"

  private val types = new ComboBox[UnitConverter]() {
    maxWidth = Double.MaxValue
    margin = Insets(3)
  }

  private val from = new TextField {
    margin = Insets(3)
    prefWidth = 200.0
  }

  private val to = new TextField {
    prefWidth = 200.0
    margin = Insets(3)
    editable = false
  }

  scene = new Scene {
    content = new GridPane {
      padding = Insets(5)

      add(new Label("Conversion type:"), 0, 0)
      add(new Label("From:"), 0, 1)
      add(new Label("To:"), 0, 2)

      add(types, 1, 0)
      add(from, 1, 1)
      add(to, 1, 2)

      add(new Button("Close") {
        // inline event handler binding
        onAction = (e: ActionEvent) => Platform.exit()
      }, 1, 3)

      columnConstraints = List(
        new ColumnConstraints {
          halignment = HPos.LEFT
          hgrow = Priority.SOMETIMES
          margin = Insets(5)
        },
        new ColumnConstraints {
          halignment = HPos.RIGHT
          hgrow = Priority.ALWAYS
          margin = Insets(5)
        }
      )
    }
  }

  // Filling the combo box
  for (converter <- converters.available) {
    types += converter
  }
  types.getSelectionModel.selectFirst()

  // Data binding
  to.text <== new StringBinding {
    bind(from.text.delegate, types.getSelectionModel.selectedItemProperty)
    def computeValue() = types.getSelectionModel.getSelectedItem.run(from.text.value)
  }
}

object PureScalaFX extends JFXApp {

  stage = new PureScalaFXView(new UnitConverters(InchesToMM, MMtoInches))

}
