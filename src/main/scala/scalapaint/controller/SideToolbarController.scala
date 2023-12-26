package scalapaint.controller

import scalapaint.model.Model
import scalapaint.tools.{FloodfillTool, PencilTool, Tool}
import scalapaint.view.CanvasPanel.Events.{MouseDragged, MousePressed, MouseReleased}
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
        case event: MousePressed => activeTool.foreach(t => t.mousePressed(event))
        case event: MouseDragged => activeTool.foreach(t => t.mouseDragged(event))
        case event: MouseReleased => activeTool.foreach(t => t.mouseReleased(event))
    }
