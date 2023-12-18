package scalapaint.controller

import scalapaint.image.filters.BlueFilter
import scalapaint.model.Model
import scalapaint.model.Model.Events.ImageUpdated
import scalapaint.view.CanvasPanel
import scalapaint.view.CanvasPanel.Events.{KeyAction, PanEvent, ZoomEvent, MousePressed}

import scala.swing.*
import scala.swing.Swing.*
import scala.swing.event.*

class CanvasPanelController(model: Model, view: CanvasPanel) extends Reactor:
    listenTo(view)
    listenTo(model)

    reactions += {
        case ImageUpdated(image) => view.updateImage(image)
        case MousePressed(point) => println(point)
    }
