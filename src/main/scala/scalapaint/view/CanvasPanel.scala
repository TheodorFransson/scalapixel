package scalapaint.view

import javafx.application.Platform
import javafx.embed.swing.{JFXPanel, SwingFXUtils}
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.image.WritableImage
import scalapaint.*
import scalapaint.image.{DrawnImage, EditorImage, RenderImage}

import java.awt.image.BufferedImage
import java.awt.{Color, Graphics2D, Image, Point}
import scala.swing.*
import scala.swing.Swing.*
import scala.swing.event.*

class CanvasPanel(initSize: Dimension, val padding: Int) extends Panel:
	import CanvasPanel.Events.*

	private val dim = new Dimension(initSize.width - (2 * padding), initSize.height - (2 * padding))
	private val renderImage: RenderImage = new RenderImage(WritableImage(dim.width, dim.height))

	val jfxPanel = new JFXPanel()
	val canvas = new Canvas(dim.width, dim.height)
	peer.add(jfxPanel)

	Platform.runLater(() => {
		val scene = new Scene(new javafx.scene.Group(canvas))
		jfxPanel.setScene(scene)

		canvas.setOnScroll(e => {
			val zoomFactor = if (e.getDeltaY < 0) 0.9 else 1.1
			zoom(zoomFactor)
			//publish(ZoomEvent(zoomFactor)) // Publish Scala event
		})

		canvas.setOnMousePressed(e => {
			publish(MousePressed(new Point(e.getX.toInt, e.getY.toInt)))
		})

		canvas.setOnMouseDragged(e => {
			publish(MouseDragged(new Point(e.getX.toInt, e.getY.toInt)))
		})

		canvas.setOnMouseReleased(e => {
			publish(MouseReleased(new Point(e.getX.toInt, e.getY.toInt)))
		})

		canvas.requestFocus()
	})

	updateSize(initSize, padding)
	background = Colors.backgroundColorDP(0)
	focusable = true

	def updateSize(size: Dimension, padding: Int): Unit =
		preferredSize = new Dimension(size.width, size.height)
		revalidate()
		EditorWindow.pack()
		repaint()

	override def paintComponent(g: Graphics2D): Unit =
		super.paintComponent(g)
		if renderImage.needsUpdate() then drawImageOnCanvas()

	def updateImage(editorImage: EditorImage): Unit =
		Platform.runLater(() => {
			val fxImage = SwingFXUtils.toFXImage(editorImage.buffer, null)
			renderImage.updateImage(fxImage)
			drawImageOnCanvas()
	})

	def drawImageOnCanvas(): Unit =
		Platform.runLater(() => {
			val graphicsContext = canvas.getGraphicsContext2D
			renderImage.render(graphicsContext)
		})

	def zoom(factor: Double): Unit =
		renderImage.zoom(factor)
		drawImageOnCanvas()

	def pan(dx: Int, dy: Int): Unit =
		renderImage.pan(dx, dy)
		drawImageOnCanvas()

object CanvasPanel:
	object Events:
		case class ZoomEvent(factor: Double) extends Event
		case class PanEvent(dx: Int, dy: Int) extends Event
		case class MousePressed(point: Point) extends Event
		case class MouseDragged(point: Point) extends Event
		case class MouseReleased(point: Point) extends Event
		case class KeyAction(event: Event) extends Event