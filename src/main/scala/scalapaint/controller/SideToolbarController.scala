package scalapaint.controller

import scalapaint.model.Model
import scalapaint.tools.{FloodfillTool, PencilTool, Tool}
import scalapaint.view.CanvasPanel.Events.{MouseDraggedCanvas, MousePressedCanvas, MouseReleasedCanvas}
import scalapaint.view.SideToolbar
import scalapaint.view.SideToolbar.Events.*

import scala.swing.{Publisher, Reactor}

class SideToolbarController(model: Model, view: SideToolbar) extends Reactor:
    var activeTool: Option[Tool] = None

    listenTo(view)
    reactions += {
        case ZoomToolActivated() => ???
        case PencilToolActivated() => activeTool = Some(new PencilTool(model))
        case FloodfillToolActivated() => activeTool = Some(new FloodfillTool(model))
        case ColorChooserActivated() => ???
        case event: MousePressedCanvas => activeTool.foreach(t => t.mousePressed(event))
        case event: MouseDraggedCanvas => activeTool.foreach(t => t.mouseDragged(event))
        case event: MouseReleasedCanvas => activeTool.foreach(t => t.mouseReleased(event))
    }
