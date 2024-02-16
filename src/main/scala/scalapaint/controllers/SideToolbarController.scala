package scalapaint.controllers

import scalapaint.EditorWindow
import scalapaint.image.ImageProcessingManager
import scalapaint.tools.fill.{FillPanel, FillTool, FillToolOperation}
import scalapaint.tools.pencil.{PencilPanel, PencilTool, PencilToolOperation}
import scalapaint.tools.Tool
import scalapaint.tools.filter.{FilterPanel, FilterTool, FilterToolOperation}
import scalapaint.tools.picker.{PickerTool, PickerToolOperation, PickerPanel}
import scalapaint.views.CanvasPanel.Events.{MouseDraggedCanvas, MousePressedCanvas, MouseReleasedCanvas}
import scalapaint.views.{ParameterPanel, SideToolbar}
import scalapaint.views.SideToolbar.Events.*

import scala.swing.{Publisher, Reactor}

class SideToolbarController(model: ImageProcessingManager, sideToolbar: SideToolbar, parameterPanel: ParameterPanel) extends Reactor:
    private var activeTool: Option[Tool] = None

    private val pencilTool = new PencilTool(new PencilToolOperation(model), new PencilPanel())
    private val floodfillTool = new FillTool(new FillToolOperation(model), new FillPanel())
    private val pickerTool = new PickerTool(new PickerToolOperation(model), new PickerPanel())
    private val filterTool = new FilterTool(new FilterToolOperation(model), new FilterPanel())

    listenTo(sideToolbar)

    reactions += {
        case PencilToolActivated() =>
            parameterPanel.replaceToolPanel(pencilTool.getPanel())
            activeTool = Some(pencilTool)
        case FloodfillToolActivated() =>
            parameterPanel.replaceToolPanel(floodfillTool.getPanel())
            activeTool = Some(floodfillTool)
        case PickerToolActivated() =>
            parameterPanel.replaceToolPanel(pickerTool.getPanel())
            activeTool = Some(pickerTool)
        case FilterToolActivated() =>
            parameterPanel.replaceToolPanel(filterTool.getPanel())
            activeTool = Some(filterTool)
        case event: MousePressedCanvas => activeTool.foreach(t => t.getOperation().mousePressed(event))
        case event: MouseDraggedCanvas => activeTool.foreach(t => t.getOperation().mouseDragged(event))
        case event: MouseReleasedCanvas => activeTool.foreach(t => t.getOperation().mouseReleased(event))
    }
