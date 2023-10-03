package imageproject.gui

import imageproject.EditorWindow
import imageproject.filters.*

import scala.swing.ListView._
import scala.swing.Swing._
import scala.swing._
import scala.swing.event._

import BorderPanel.Position.*

import java.awt.{Color}
import java.awt.BorderLayout

class FilterPanel extends BoxPanel(Orientation.Vertical):
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

    val argsField = new TextField:
        text = "4"

    val argsFlowpanel = new FlowPanel:
        peer.setAlignmentX(java.awt.Component.RIGHT_ALIGNMENT)
        contents += new Label:
            text = "Arguments"
        contents += argsField
  
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


    val panels = Seq(comboFlowpanel, argsFlowpanel, previewFlowpanel, applyFlowpanel)
    panels.foreach(p => p.maximumSize = p.preferredSize)

    contents ++= panels

    listenTo(comboBox.selection, previewCheckbox, applyButton, argsField)
    reactions += {
        case ButtonClicked(`applyButton`) => applyFilter()
        case ButtonClicked(`previewCheckbox`) => 
            if previewCheckbox.selected then EditorWindow.getDrawnImage().addFilter(comboBox.selection.item)
            else EditorWindow.getDrawnImage().removeFilter()
        case SelectionChanged(`comboBox`) =>  
            if previewCheckbox.selected then EditorWindow.getDrawnImage().addFilter(comboBox.selection.item)
        case ValueChanged(`argsField`) => EditorWindow.repaintCanvas()
    }
    
    private def applyFilter(): Unit =
        getArgs() match
            case Some(arg) => 
                EditorWindow.setCurrentImage(comboBox.selection.item.apply(EditorWindow.getCurrentImage(), arg))
            case None => 
                EditorWindow.setCurrentImage(comboBox.selection.item.apply(EditorWindow.getCurrentImage()))

    def getArgs(): Option[Double] = argsField.text.toDoubleOption

        