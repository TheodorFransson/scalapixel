package scalapaint.view

import scalapaint.{EditorWindow, EventBinder}
import scalapaint.image.filters.*

import java.awt.{BorderLayout, Color}
import scala.swing.*
import scala.swing.BorderPanel.Position.*
import scala.swing.ListView.*
import scala.swing.Swing.*
import scala.swing.event.*

class FilterPanel extends BoxPanel(Orientation.Vertical) with EventBinder:
    import FilterPanel.Events.* 

    val filters: Array[ImageFilter] = Array(
        new BlueFilter(),
        new CryptographyFilter(), 
        new GaussFilter(), 
        new GrayscaleFilter(), 
        new InvertFilter(), 
        new SobelFilter())

    val comboBox = new ComboBox[ImageFilter](filters):
        selection.item = filters(2)
    
    val comboFlowpanel = new FlowPanel:
        peer.setAlignmentX(java.awt.Component.RIGHT_ALIGNMENT)
        contents += new Label:
            text = "Filter"
        contents += comboBox

    val optionField = new TextField:
        text = ""

    val optionFlowpanel = new FlowPanel:
        peer.setAlignmentX(java.awt.Component.RIGHT_ALIGNMENT)
        contents += new Label:
            text = "Arguments"
        contents += optionField
  
    val previewCheckbox = new CheckBox:
        selected = false

    val previewFlowpanel = new FlowPanel:
        peer.setAlignmentX(java.awt.Component.RIGHT_ALIGNMENT)
        contents += new Label:
            text = "Preview"
        contents += previewCheckbox

    val applyButton = new Button:
        text = "Apply filter"

    val applyFlowpanel= new FlowPanel:
        peer.setAlignmentX(java.awt.Component.RIGHT_ALIGNMENT)
        contents += applyButton

    bindToEvent(applyButton, ApplyFilter())
    bindToValueChangeEvent(previewCheckbox, PreviewModeChanged.apply, () => previewCheckbox.selected)
    bindToValueChangeEvent(comboBox.selection, FilterSelectionChanged.apply, () => comboBox.selection.item)
    bindToValueChangeEvent(optionField, FilterParameterChanged.apply, () => optionField.text)

    val panels = Seq(comboFlowpanel, optionFlowpanel, previewFlowpanel, applyFlowpanel)
    panels.foreach(p => p.maximumSize = p.preferredSize)

    contents ++= panels

object FilterPanel:
    object Events:
        case class ApplyFilter() extends Event
        case class PreviewModeChanged(mode: Boolean) extends Event
        case class FilterSelectionChanged(selection: ImageFilter) extends Event
        case class FilterParameterChanged(value: String) extends Event