package scalapaint.view

import scalapaint.*
import scalapaint.image.{EditorImage, RenderImage}

import java.awt.{Dimension, Graphics2D}
import scala.swing.*
import scala.swing.Swing.*
import scala.swing.event.{Event, KeyPressed, KeyReleased, MouseDragged, MouseEvent, MousePressed, MouseReleased, MouseWheelMoved}

class CanvasPanel(initSize: Dimension, val padding: Int) extends Panel:
	import CanvasPanel.Events.*

	private var dim = new Dimension(initSize.width, initSize.height)
	private val renderImage: RenderImage = new RenderImage(EditorImage.ofDim(400, 400))

	updateSize(initSize, padding)
	background = Colors.backgroundColorDP(0)
	focusable = true

	def updateSize(size: Dimension, padding: Int = padding): Unit =
		dim = new Dimension(size.width, size.height)
		preferredSize = new Dimension(dim.width, dim.height)

		revalidate()
		EditorWindow.pack()
		repaint()

	override def paintComponent(g: Graphics2D): Unit =
		super.paintComponent(g)
		g.setClip(0, 0, dim.width, dim.height)

		renderImage.render(g, dim)

	def updateImage(editorImage: EditorImage, area: Rectangle): Unit =
		renderImage.updateImage(editorImage)

		repaint()

	def setNewImage(editorImage: EditorImage): Unit =
		renderImage.updateImage(editorImage)
		resetViewTransform()
		repaint()

	def resetViewTransform(): Unit = renderImage.reset(preferredSize)

	def zoom(factor: Double, target: Point = new Point(dim.width / 2, dim.height / 2)): Unit =
		renderImage.zoom(factor, target, dim)
		repaint()

	def pan(dx: Int, dy: Int): Unit =
		renderImage.pan(dx, dy)
		repaint()

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