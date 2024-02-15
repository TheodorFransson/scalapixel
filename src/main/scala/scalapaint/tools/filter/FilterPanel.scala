package scalapaint.tools.filter

import scalapaint.image.filters.*
import scalapaint.{EditorWindow, EventBinder}

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
        maximumSize = preferredSize
    
    val comboFlowpanel = new BoxPanel(Orientation.Horizontal):
        contents += new Label("Filter")
        contents += HGlue
        contents += comboBox

    val optionField = new TextField:
        text = ""
        maximumSize = preferredSize

    val optionFlowpanel = new BoxPanel(Orientation.Horizontal):
        contents += new Label("Arguments")
        contents += HGlue
        contents += optionField
  
    val previewCheckbox = new CheckBox:
        selected = false

    val previewFlowpanel = new BoxPanel(Orientation.Horizontal):
        contents += new Label("Preview")
        contents += HGlue
        contents += previewCheckbox

    val applyButton = new Button:
        text = "Apply filter"

    bindToEvent(applyButton, ApplyFilter())
    bindToValueChangeEvent(previewCheckbox, PreviewModeChanged.apply, () => previewCheckbox.selected)
    bindToValueChangeEvent(comboBox.selection, FilterSelectionChanged.apply, () => comboBox.selection.item)
    bindToValueChangeEvent(optionField, FilterParameterChanged.apply, () => optionField.text)

    contents += VStrut(10)
    contents += comboFlowpanel
    contents += VStrut(5)
    contents += optionFlowpanel
    contents += VStrut(5)
    contents += applyButton
    contents += VGlue

    def getParameterValue: String = optionField.text

object FilterPanel:
    object Events:
        case class ApplyFilter() extends Event
        case class PreviewModeChanged(mode: Boolean) extends Event
        case class FilterSelectionChanged(selection: ImageFilter) extends Event
        case class FilterParameterChanged(value: String) extends Event