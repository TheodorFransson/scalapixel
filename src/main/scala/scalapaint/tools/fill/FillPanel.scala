package scalapaint.tools.fill

import scalapaint.EventBinder
import scalapaint.tools.{ParameterGridPanel, ToolPanel}

import scala.swing.Swing.{HGlue, VGlue, VStrut}
import scala.swing.event.Event
import scala.swing.{BoxPanel, FlowPanel, Label, Orientation, Slider, TextField}

class FillPanel extends ToolPanel("Bucket fill") with EventBinder:
  import FillPanel.Events.*

  private val toleranceSlider = new Slider:
    min = 0
    max = 255
    value = 0
    majorTickSpacing = 50

  private val components = Seq(
    ("Tolerance", toleranceSlider)
  )

  private val gridPanel = new ParameterGridPanel(components)

  contents += gridPanel
  contents += VGlue

  bindToValueChangeEvent(toleranceSlider, SliderValueChanged.apply, () => toleranceSlider.value)

object FillPanel:
  object Events:
    case class SliderValueChanged(newValue: Int) extends Event