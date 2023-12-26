package scalapaint.view

import javafx.application.Platform
import javafx.embed.swing.{JFXPanel, SwingFXUtils}
import javafx.scene.canvas.Canvas
import javafx.scene.image.WritableImage
import javafx.scene.input.{KeyEvent, MouseButton, MouseEvent, ScrollEvent}
import javafx.scene.{Scene, paint}
import scalapaint.*
import scalapaint.image.{EditorImage, RenderImage}

import java.awt.{Dimension, FlowLayout, Graphics2D}
import scala.swing.*
import scala.swing.Swing.*
import scala.swing.event.Event

class CanvasPanel(initSize: Dimension, val padding: Int) extends Panel:
	import CanvasPanel.Events.*

	private val layout = new FlowLayout()

	private var dim = new Dimension(initSize.width, initSize.height)
	private val renderImage: RenderImage = new RenderImage(WritableImage(dim.width, dim.height))

	private val jfxPanel = new JFXPanel()
	private val canvas = new Canvas(dim.width, dim.height)

	layout.setVgap(1)
	peer.setLayout(layout)

	updateSize(initSize, padding)
	background = Colors.backgroundColorDP(0)
	focusable = true

	peer.add(jfxPanel)

	Platform.runLater(() => {
		val scene = new Scene(new javafx.scene.Group(canvas))
		jfxPanel.setScene(scene)

		canvas.setOnScroll(e => publish(ZoomEvent(e)))
		canvas.setOnMousePressed(e => publish(MousePressed(e, renderImage.getPointOnImage(getPointFromEvent(e)))))
		canvas.setOnMouseDragged(e => publish(MouseDragged(e, renderImage.getPointOnImage(getPointFromEvent(e)))))
		canvas.setOnMouseReleased(e => publish(MouseReleased(e, renderImage.getPointOnImage(getPointFromEvent(e)))))
		canvas.setOnKeyPressed(e => publish(KeyPressed(e)))
		canvas.setOnKeyReleased(e => publish(KeyReleased(e)))
		canvas.requestFocus()
	})

	def getPointFromEvent(event: MouseEvent): Point = new Point(event.getX.toInt, event.getY.toInt)

	def updateSize(size: Dimension, padding: Int = padding): Unit =
		dim = new Dimension(size.width, size.height)
		preferredSize = new Dimension(dim.width, dim.height)
		jfxPanel.setPreferredSize(new Dimension(dim.width, dim.height))

		Platform.runLater(() => {
			canvas.setWidth(jfxPanel.getWidth)
			canvas.setHeight(jfxPanel.getHeight)
		})

		revalidate()
		EditorWindow.pack()
		drawImageOnCanvas()
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

	def setNewImage(editorImage: EditorImage): Unit =
		updateImage(editorImage)
		renderImage.reset(dim, new Dimension(editorImage.width, editorImage.height))

	def resetViewTransform(): Unit =
		renderImage.reset(dim)

	private def drawImageOnCanvas(): Unit =
		Platform.runLater(() => {
			val graphicsContext = canvas.getGraphicsContext2D

			graphicsContext.setFill(
				new javafx.scene.paint.Color(
					background.getRed().toDouble / 255.0,
					background.getBlue().toDouble / 255.0,
					background.getGreen().toDouble / 255.0, 1.0
				)
			)

			graphicsContext.fillRect(0, 0, canvas.getWidth, canvas.getHeight)

			renderImage.render(graphicsContext)
		})

	def zoom(factor: Double, target: Point = new Point(dim.width / 2, dim.height / 2)): Unit =
		renderImage.zoom(factor, target, dim)
		drawImageOnCanvas()

	def pan(dx: Int, dy: Int): Unit =
		renderImage.pan(dx, dy)
		drawImageOnCanvas()

object CanvasPanel:
	object Events:
		case class ZoomEvent(originalEvent: ScrollEvent) extends Event
		case class MousePressed(originalEvent: MouseEvent, pointOnImage: Point) extends Event
		case class MouseDragged(originalEvent: MouseEvent, pointOnImage: Point) extends Event
		case class MouseReleased(originalEvent: MouseEvent, pointOnImage: Point) extends Event
		case class KeyAction(event: Event) extends Event
		case class KeyPressed(originalEvent: KeyEvent) extends Event
		case class KeyReleased(originalEvent: KeyEvent) extends Event