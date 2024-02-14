package scalapaint.tools.fill

import scalapaint.EventBinder

import scala.swing.Swing.{HGlue, VGlue, VStrut}
import scala.swing.event.Event
import scala.swing.{BoxPanel, FlowPanel, Label, Orientation, Slider, TextField}

class FillPanel extends BoxPanel(Orientation.Vertical) with EventBinder:
  import FillPanel.Events.*

  private val toleranceSlider = new Slider:
    min = 0
    max = 255
    value = 0
    majorTickSpacing = 50

  private val tolerancePanel = new BoxPanel(Orientation.Horizontal):
    contents += new Label("Tolerance")
    contents += HGlue
    contents += toleranceSlider

  contents += VStrut(10)
  contents += tolerancePanel
  contents += VGlue

  bindToValueChangeEvent(toleranceSlider, SliderValueChanged.apply, () => toleranceSlider.value)

object FillPanel:
  object Events:
    case class SliderValueChanged(newValue: Int) extends Event