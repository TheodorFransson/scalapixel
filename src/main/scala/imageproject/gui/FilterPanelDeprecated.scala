package imageproject.gui

import imageproject.EditorWindow

import scala.swing.ListView._
import scala.swing.Swing._
import scala.swing._
import scala.swing.event._

import BorderPanel.Position.*

import java.awt.{Color}
import java.awt.BorderLayout
import imageproject.image.filters.{BlueFilter, SobelFilter, GaussFilter, CryptographyFilter, GrayscaleFilter, ImageFilter, InvertFilter}

class FilterPanelDeprecated extends BoxPanel(Orientation.Vertical):
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
        text = "4"

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


    val panels = Seq(comboFlowpanel, optionFlowpanel, previewFlowpanel, applyFlowpanel)
    panels.foreach(p => p.maximumSize = p.preferredSize)

    contents ++= panels

    listenTo(comboBox.selection, previewCheckbox, applyButton, optionField)
    reactions += {
        case ButtonClicked(`applyButton`) => applyFilter()
        case ButtonClicked(`previewCheckbox`) => 
            if previewCheckbox.selected then EditorWindow.getDrawnImage().addFilter(comboBox.selection.item)
            else EditorWindow.getDrawnImage().removeFilter()
        case SelectionChanged(`comboBox`) =>  
            if previewCheckbox.selected then EditorWindow.getDrawnImage().addFilter(comboBox.selection.item)
        case ValueChanged(`optionField`) => EditorWindow.repaintCanvas()
    }
    
    private def applyFilter(): Unit =
        getOption() match
            case Some(arg) => 
                comboBox.selection.item.setOption(arg)
                EditorWindow.setCurrentImage(comboBox.selection.item.process(EditorWindow.getCurrentImage()))
            case None => 
                EditorWindow.setCurrentImage(comboBox.selection.item.process(EditorWindow.getCurrentImage()))

    def getOption(): Option[Double] = optionField.text.toDoubleOption

        