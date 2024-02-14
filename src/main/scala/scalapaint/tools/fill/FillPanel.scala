package scalapaint.tools.fill

import scala.swing.event.Event
import scala.swing.{BoxPanel, FlowPanel, Label, Orientation, TextField}

class FillPanel extends BoxPanel(Orientation.Horizontal):
  import FillPanel.Events.*

  private val tolerance = new TextField:
    text = "50"

  private val toleranceFlowpanel = new FlowPanel:
    peer.setAlignmentX(java.awt.Component.RIGHT_ALIGNMENT)

    contents += new Label:
      text = "Tolerance"

  toleranceFlowpanel.maximumSize = toleranceFlowpanel.preferredSize
  contents += toleranceFlowpanel

object FillPanel:
  object Events:
    case class SliderValueChanged(newValue: Int) extends Event