package scalapaint.views

import scalapaint.*
import scalapaint.image.{EditorImage, RenderImage}

import java.awt.Graphics2D
import scala.swing.{Dimension, Panel, Point, Rectangle}
import scala.swing.Swing.*
import scala.swing.event.{Event, KeyPressed, KeyReleased, MouseDragged, MouseEvent, MousePressed, MouseReleased, MouseWheelMoved}

class CanvasPanel extends Panel:
	import CanvasPanel.Events.*

	background = Colors.backgroundColorAtDepth(0)
	focusable = true

	private def dimension = size
	private val renderImage: RenderImage = new RenderImage(EditorImage.ofDim(400, 400))

	override def paintComponent(g: Graphics2D): Unit =
		super.paintComponent(g)
		g.setClip(0, 0, dimension.width, dimension.height)

		renderImage.render(g, dimension)

	def updateImage(editorImage: EditorImage): Unit =
		renderImage.updateImage(editorImage)

		repaint()

	def setNewImage(editorImage: EditorImage): Unit =
		renderImage.updateImage(editorImage)
		resetViewTransform()

	def resetViewTransform(): Unit =
		renderImage.reset(dimension)
		repaint()

	def zoom(factor: Double, target: Point = new Point(dimension.width / 2, dimension.height / 2)): Unit =
		renderImage.zoom(factor, target, dimension)
		repaint()

	def zoomAbsolute(zoom: Int): Unit =
		renderImage.zoomAbsolute(zoom, dimension)
		repaint()

	def pan(dx: Int, dy: Int): Unit =
		renderImage.pan(dx, dy, dimension)
		repaint()

	def getPositionalParameters(): (Dimension, Rectangle, Point) = (dimension, renderImage.getBounds(dimension), renderImage.getPosition)

	def getZoomFactor(): Int = (renderImage.getZoomFactor * 10).toInt

	listenTo(mouse.clicks, mouse.wheel, mouse.moves, keys)
	reactions += {
		case e: MousePressed =>
			peer.requestFocus()
			publish(MousePressedCanvas(e, renderImage.getPointOnImage(e.point)))
		case e: MouseDragged => publish(MouseDraggedCanvas(e, renderImage.getPointOnImage(e.point)))
		case e: MouseReleased => publish(MouseReleasedCanvas(e, renderImage.getPointOnImage(e.point)))
		case e: MouseWheelMoved => publish(ZoomEvent(e))
		case e: KeyPressed => publish(KeyPressedCanvas(e))
		case e: KeyReleased => publish(KeyReleasedCanvas(e))
	}

object CanvasPanel:
	object Events:
		case class ZoomEvent(originalEvent: MouseWheelMoved) extends Event
		case class MousePressedCanvas(originalEvent: MousePressed, pointOnImage: Point) extends Event
		case class MouseDraggedCanvas(originalEvent: MouseDragged, pointOnImage: Point) extends Event
		case class MouseReleasedCanvas(originalEvent: MouseReleased, pointOnImage: Point) extends Event
		case class KeyAction(event: Event) extends Event
		case class KeyPressedCanvas(originalEvent: scala.swing.event.KeyPressed) extends Event
		case class KeyReleasedCanvas(originalEvent: scala.swing.event.KeyReleased) extends Event