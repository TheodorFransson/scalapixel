package scalapixel.controllers

import scalapixel.image.ImageProcessingManager
import ImageProcessingManager.Events.*
import scalapixel.views.{CanvasPanel, NavigablePanel}
import scalapixel.views.CanvasPanel.Events.*
import scalapixel.views.NavigablePanel.Events.*
import scalapixel.controllers.FocusListener
import scalapixel.controllers.FocusListener.Events.*

import java.awt.event.*
import java.util.TimerTask
import scala.swing.*
import scala.swing.event.{Key, MouseEvent, MouseWheelMoved, UIElementResized}

class CanvasPanelController(model: ImageProcessingManager, canvasPanel: CanvasPanel, navigablePanel: NavigablePanel) extends FocusListener:
    private var mouseOrigin = new Point(0, 0)
    private var dragging = false

    private val panTimer = new java.util.Timer()
    private var panTask: Option[TimerTask] = None
    private val pressedKeys = scala.collection.mutable.Set[event.Key.Value]()

    listenTo(model, canvasPanel, navigablePanel)
    listenToFocusEvents(canvasPanel)

    reactions += {
        case ImageUpdated(image) => canvasPanel.updateImage(image)
        case NewImage(image) =>
          canvasPanel.setNewImage(image)
          reset()
        case UIElementResized(_) =>
          updateScrollBars()
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
        case HorizontalScroll(value) =>
          canvasPanel.pan(-value, 0)
        case VerticalScroll(value) =>
          canvasPanel.pan(0, -value)
        case Zoom(value) =>
          canvasPanel.zoomAbsolute(value)
          updateScrollBars()
        case FocusLost() =>
          stopPanningOnFocusLost()
    }

    def dispose(): Unit = {
      panTask.foreach(_.cancel())
      panTimer.cancel()
      panTimer.purge()
    }

    private def zoom(event: MouseWheelMoved): Unit =
      val target = event.point
      val zoomFactor = if (event.rotation > 0) 0.9 else 1.1
      canvasPanel.zoom(zoomFactor, target)
      navigablePanel.setZoom(canvasPanel.getZoomFactor())
      updateScrollBars()

    private def callPanOnCanvasPanel(dx: Int, dy: Int): Unit =
      canvasPanel.pan(dx, dy)
      updateScrollBars()

    private def stopPanning(event: MouseEvent): Unit =
      callPanOnCanvasPanel(event.point.x - mouseOrigin.x, event.point.y - mouseOrigin.y)
      dragging = false

    private def stopPanningOnFocusLost(): Unit =
      panTask.foreach(_.cancel())
      panTask = None

      dragging = false

      pressedKeys.clear()

    private def pan(event: MouseEvent): Unit =
      if dragging then
        callPanOnCanvasPanel(event.point.x - mouseOrigin.x, event.point.y - mouseOrigin.y)

    private def startPanning(event: MouseEvent): Unit =
      dragging = true

    private def keyPressedActions(): Unit =
      checkReset()
      panWithKeys()

    private def checkReset(): Unit = if pressedKeys(Key.R) then reset()

    private def reset(): Unit =
      canvasPanel.resetViewTransform()
      navigablePanel.setZoom(canvasPanel.getZoomFactor())
      updateScrollBars()

    private def panWithKeys(): Unit =
        if (panTask.isEmpty && pressedKeys.nonEmpty) then
            val task = new TimerTask:
                def run(): Unit =

                  val direction = new Point(0, 0)
                  if (pressedKeys(Key.W) || pressedKeys(Key.Up)) direction.y += 1
                  if (pressedKeys(Key.A) || pressedKeys(Key.Left)) direction.x += 1
                  if (pressedKeys(Key.S) || pressedKeys(Key.Down)) direction.y -= 1
                  if (pressedKeys(Key.D) || pressedKeys(Key.Right)) direction.x -= 1

                  if (direction.x != 0 || direction.y != 0) callPanOnCanvasPanel(direction.x, direction.y)
            panTimer.schedule(task, 0, 5)
            panTask = Some(task)

    private def updateScrollBars(): Unit =
      val (canvasPanelDimension, imageBounds, imagePosition): (Dimension, Rectangle, Point) =
        canvasPanel.getPositionalParameters()

      val horizontalValue = imageBounds.x + imageBounds.width - imagePosition.x
      val horizontalExtent = canvasPanelDimension.width / 2
      val verticalValue = imageBounds.y + imageBounds.height - imagePosition.y
      val verticalExtent = canvasPanelDimension.height / 2

      navigablePanel.setHorizontalScrollbarParameters(
        horizontalValue,
        horizontalExtent,
        imageBounds.x,
        imageBounds.width + horizontalExtent)
      navigablePanel.setVerticalScrollbarParameters(
        verticalValue,
        verticalExtent,
        imageBounds.y,
        imageBounds.height + verticalExtent)
