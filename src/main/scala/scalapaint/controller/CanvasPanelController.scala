package scalapaint.controller


import javafx.scene.input.{KeyCode, KeyEvent, MouseButton, MouseEvent, ScrollEvent}
import scalapaint.image.filters.BlueFilter
import scalapaint.model.Model
import scalapaint.model.Model.Events.*
import scalapaint.view.CanvasPanel
import scalapaint.view.CanvasPanel.Events

import java.util.TimerTask
import scala.swing.*
import scala.swing.Swing.*
import scala.swing.event.UIElementResized

class CanvasPanelController(model: Model, view: CanvasPanel) extends Reactor:
    private var mouseOrigin = new Point(0, 0)
    private var dragging = false

    private val panTimer = new java.util.Timer()
    private var panTask: Option[TimerTask] = None
    private val pressedKeys = scala.collection.mutable.Set[KeyCode]()

    listenTo(view)
    listenTo(model)

    reactions += {
        case ImageUpdated(image) => view.updateImage(image)
        case UIElementResized(_) =>
          val newSize = view.peer.getSize()
          view.updateSize(new Dimension(newSize.width, newSize.height))
        case Events.ZoomEvent(event) => zoom(event)
        case Events.MousePressed(event) =>
          if event.getButton == MouseButton.MIDDLE then panWithMouse(event)
          mouseOrigin = new Point(event.getX.toInt, event.getY.toInt)
        case Events.MouseDragged(event) =>
          if event.getButton == MouseButton.MIDDLE then panWithMouse(event)
          mouseOrigin = new Point(event.getX.toInt, event.getY.toInt)
        case Events.MouseReleased(event) =>
          if event.getButton == MouseButton.MIDDLE then panWithMouse(event)
          mouseOrigin = new Point(event.getX.toInt, event.getY.toInt)
        case Events.KeyPressed(event) =>
          pressedKeys += event.getCode
          updatePanning()
        case Events.KeyReleased(event) =>
          pressedKeys -= event.getCode
          updatePanning()
    }

    def zoom(event: ScrollEvent): Unit =
        val target = new Point(event.getX.toInt, event.getY.toInt)
        val zoomFactor = if (event.getDeltaY < 0) 0.9 else 1.1
        view.zoom(zoomFactor, target)

    private def panWithMouse(event: MouseEvent): Unit =
        val point = new Point(event.getX.toInt, event.getY.toInt)

        if event.getEventType == MouseEvent.MOUSE_RELEASED then
            view.pan(point.x - mouseOrigin.x, point.y - mouseOrigin.y)
            dragging = false
        else if dragging then
            view.pan(point.x - mouseOrigin.x, point.y - mouseOrigin.y)
        else
            dragging = true

    private def updatePanning(): Unit =
        if (panTask.isEmpty && pressedKeys.nonEmpty) then
            val task = new TimerTask:
                def run(): Unit =

                  val direction = new Point(0, 0)
                  if (pressedKeys(KeyCode.W) || pressedKeys(KeyCode.UP)) direction.y += 10
                  if (pressedKeys(KeyCode.A) || pressedKeys(KeyCode.LEFT)) direction.x += 10
                  if (pressedKeys(KeyCode.S) || pressedKeys(KeyCode.DOWN)) direction.y -= 10
                  if (pressedKeys(KeyCode.D) || pressedKeys(KeyCode.RIGHT)) direction.x -= 10

                  view.pan(direction.x, direction.y)
            panTimer.schedule(task, 0, 25) // Adjust the period for faster/slower panning
            panTask = Some(task)
