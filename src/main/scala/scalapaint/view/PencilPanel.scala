package scalapaint.view

import scalapaint.EventBinder

import java.awt.Color
import javax.swing.{JSpinner, SpinnerNumberModel}
import scala.swing.*
import scala.swing.BorderPanel.Position.*
import scala.swing.Swing.*
import scala.swing.event.*

class PencilPanel extends BoxPanel(Orientation.Vertical) with EventBinder:
    import PencilPanel.Events.*

    private val widthFeild = new TextField:
        text = "5"
        
    private val model = new SpinnerNumberModel(5, 1, 100, 1)
    private val spinner = new JSpinner(model)

    private var widthFlowpanel = new FlowPanel:
        peer.setAlignmentX(java.awt.Component.RIGHT_ALIGNMENT)

        contents += new Label:
            text = "Width"

        peer.add(spinner)

    widthFlowpanel.maximumSize = widthFlowpanel.preferredSize
    contents += widthFlowpanel

    bindToSpinnerChangeEvent[Int](spinner, SpinnerValueChanged.apply, model.getNumber.intValue)

    def setValue(value: Int): Unit = 
        model.setValue(value)

object PencilPanel:
    object Events:
        case class SpinnerValueChanged(newValue: Int) extends Event