package scalapaint.controller


import javafx.scene.input.{MouseButton, MouseEvent, ScrollEvent}
import scalapaint.image.filters.BlueFilter
import scalapaint.model.Model
import scalapaint.model.Model.Events.*
import scalapaint.view.CanvasPanel
import scalapaint.view.CanvasPanel.Events

import scala.swing.*
import scala.swing.Swing.*
import scala.swing.event.UIElementResized

class CanvasPanelController(model: Model, view: CanvasPanel) extends Reactor:
    private var mouseOrigin = new Point(0, 0)
    private var dragging = false

    listenTo(view)
    listenTo(model)

    reactions += {
        case ImageUpdated(image) => view.updateImage(image)
        case UIElementResized(_) =>
          val newSize = view.peer.getSize()
          view.updateSize(new Dimension(newSize.width, newSize.height))
        case Events.ZoomEvent(event) => zoom(event)
        case Events.MousePressed(event) =>
          if event.getButton == MouseButton.MIDDLE then pan(event)
          mouseOrigin = new Point(event.getX.toInt, event.getY.toInt)
        case Events.MouseDragged(event) =>
          if event.getButton == MouseButton.MIDDLE then pan(event)
          mouseOrigin = new Point(event.getX.toInt, event.getY.toInt)
        case Events.MouseReleased(event) =>
          if event.getButton == MouseButton.MIDDLE then pan(event)
          mouseOrigin = new Point(event.getX.toInt, event.getY.toInt)
    }

    def zoom(event: ScrollEvent): Unit =
      val zoomFactor = if (event.getDeltaY < 0) 0.9 else 1.1
      view.zoom(zoomFactor)

    def pan(event: MouseEvent): Unit =
        val point = new Point(event.getX.toInt, event.getY.toInt)

        if event.getEventType == MouseEvent.MOUSE_RELEASED then
            view.pan(point.x - mouseOrigin.x, point.y - mouseOrigin.y)
            dragging = false
        else if dragging then
            view.pan(point.x - mouseOrigin.x, point.y - mouseOrigin.y)
        else
            dragging = true
