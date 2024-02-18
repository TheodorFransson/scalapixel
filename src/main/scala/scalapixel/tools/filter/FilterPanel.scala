package scalapixel.tools.filter

import scalapixel.image.filters.*
import scalapixel.tools.ToolPanel
import scalapixel.tools.components.{ParameterGridPanel, TooltipComboBox}
import scalapixel.{EditorWindow, EventBinder}

import java.awt.{BorderLayout, Color}
import scala.swing.*
import scala.swing.BorderPanel.Position.*
import scala.swing.ListView.*
import scala.swing.Swing.*
import scala.swing.event.*

class FilterPanel extends ToolPanel("Filters") with EventBinder:
    import FilterPanel.Events.* 

    val filters: Array[ImageFilter] = Array(
        new TintFilter(),
        new CryptographyFilter(), 
        new GaussFilter(), 
        new GrayscaleFilter(), 
        new InvertFilter(), 
        new SobelFilter())

    val comboBox = new TooltipComboBox[ImageFilter](filters, filters.map(_.description)):
        selection.item = filters(2)
        maximumSize = preferredSize

    val optionField = new TextField:
        text = ""
        maximumSize = preferredSize
  
    val previewCheckbox = new CheckBox:
        selected = false

    val applyButton = new Button:
        text = "Apply filter"
        maximumSize = preferredSize

    val compontents = Seq(
        ("Filter", comboBox),
        ("Arguments", optionField)
    )

    val gridPanel = new ParameterGridPanel(compontents)
    val horizontalBoxPanel = new BoxPanel(Orientation.Horizontal):
        xLayoutAlignment = Alignment.Center.id
        contents += HGlue
        contents += applyButton
        contents += HGlue
    val verticalBoxPanel = new BoxPanel(Orientation.Vertical):
        contents += VStrut(5)
        contents += horizontalBoxPanel

    val borderPanel = new BorderPanel:
        layout(gridPanel) = North
        layout(verticalBoxPanel) = Center

    contents += borderPanel

    bindToEvent(applyButton, ApplyFilter())
    bindToValueChangeEvent(previewCheckbox, PreviewModeChanged.apply, () => previewCheckbox.selected)
    bindToValueChangeEvent(comboBox.selection, FilterSelectionChanged.apply, () => comboBox.selection.item)
    bindToValueChangeEvent(optionField, FilterParameterChanged.apply, () => optionField.text)

    def getParameterValue: String = optionField.text

object FilterPanel:
    object Events:
        case class ApplyFilter() extends Event
        case class PreviewModeChanged(mode: Boolean) extends Event
        case class FilterSelectionChanged(selection: ImageFilter) extends Event
        case class FilterParameterChanged(value: String) extends Event