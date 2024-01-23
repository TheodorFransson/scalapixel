package scalapaint.controller

import scalapaint.model.Model
import scalapaint.model.Model.Events.*
import scalapaint.view.CanvasPanel
import scalapaint.view.CanvasPanel.Events.*

import java.awt.event.*
import java.util.TimerTask
import scala.swing.*
import scala.swing.event.{Key, KeyPressed, MouseEvent, MouseWheelMoved, UIElementResized}

class CanvasPanelController(model: Model, view: CanvasPanel) extends Reactor:
    private var mouseOrigin = new Point(0, 0)
    private var dragging = false

    private val panTimer = new java.util.Timer()
    private var panTask: Option[TimerTask] = None
    private val pressedKeys = scala.collection.mutable.Set[event.Key.Value]()

    listenTo(view)
    listenTo(model)

    reactions += {
        case ImageUpdated(image) => view.updateImage(image)
        case NewImage(image) => view.setNewImage(image)
        case UIElementResized(_) =>
          val newSize = view.peer.getSize()
          view.updateSize(new Dimension(newSize.width, newSize.height))
        case ZoomEvent(event) => zoom(event)
        case MousePressedCanvas(event, point) =>
          if event.peer.getButton == MouseEvent.BUTTON2 then startPanning(event)
          mouseOrigin = event.point
        case MouseDraggedCanvas(event, point) =>
          pan(event)
          mouseOrigin = event.point
        case MouseReleasedCanvas(event, point) =>
          if event.peer.getButton == MouseEvent.BUTTON2 then stopPanning(event)
          mouseOrigin = event.point
        case KeyPressedCanvas(event) =>
          pressedKeys += event.key
          keyPressedActions()
        case KeyReleasedCanvas(event) =>
          pressedKeys -= event.key
    }

    def zoom(event: MouseWheelMoved): Unit =
        val target = event.point
        val zoomFactor = if (event.rotation > 0) 0.9 else 1.1
        view.zoom(zoomFactor, target)

    private def stopPanning(event: MouseEvent): Unit =
        view.pan(event.point.x - mouseOrigin.x, event.point.y - mouseOrigin.y)
        dragging = false

    private def pan(event: MouseEvent): Unit =
        if dragging then view.pan(event.point.x - mouseOrigin.x, event.point.y - mouseOrigin.y)

    private def startPanning(event: MouseEvent): Unit =
        dragging = true

    private def keyPressedActions(): Unit =
      checkReset()
      panWithKeys()

    private def checkReset(): Unit =
      if pressedKeys(Key.R) then view.resetViewTransform()

    private def panWithKeys(): Unit =
        if (panTask.isEmpty && pressedKeys.nonEmpty) then
            val task = new TimerTask:
                def run(): Unit =

                  val direction = new Point(0, 0)
                  if (pressedKeys(Key.W) || pressedKeys(Key.Up)) direction.y += 1
                  if (pressedKeys(Key.A) || pressedKeys(Key.Left)) direction.x += 1
                  if (pressedKeys(Key.S) || pressedKeys(Key.Down)) direction.y -= 1
                  if (pressedKeys(Key.D) || pressedKeys(Key.Right)) direction.x -= 1

                  view.pan(direction.x, direction.y)
            panTimer.schedule(task, 0, 5) // Adjust the period for faster/slower panning
            panTask = Some(task)

    def dispose(): Unit = {
      panTask.foreach(_.cancel())
      panTimer.cancel()
      panTimer.purge()
    }
