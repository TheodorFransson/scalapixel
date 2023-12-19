package scalapaint.view

import javafx.application.Platform
import javafx.embed.swing.{JFXPanel, SwingFXUtils}
import javafx.scene.canvas.Canvas
import javafx.scene.image.WritableImage
import javafx.scene.input.{KeyEvent, MouseButton, MouseEvent, ScrollEvent}
import javafx.scene.{Scene, paint}
import scalapaint.*
import scalapaint.image.{DrawnImage, EditorImage, RenderImage}

import java.awt.image.BufferedImage
import java.awt.{Dimension, FlowLayout, Graphics2D}
import javax.swing.SwingUtilities
import javax.swing.border.EmptyBorder
import scala.swing.*
import scala.swing.BorderPanel.Position.Center
import scala.swing.Swing.*
import scala.swing.event.*

class CanvasPanel(initSize: Dimension, val padding: Int) extends Panel:
	import CanvasPanel.Events.*

	private val layout = new FlowLayout()

	private val dim = new Dimension(initSize.width, initSize.height)
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
		canvas.setOnMousePressed(e => publish(MousePressed(e)))
		canvas.setOnMouseDragged(e => publish(MouseDragged(e)))
		canvas.setOnMouseReleased(e => publish(MouseReleased(e)))
		canvas.setOnKeyPressed(e => publish(KeyPressed(e)))
		canvas.setOnKeyReleased(e => publish(KeyReleased(e)))
		canvas.requestFocus()
	})

	def updateSize(size: Dimension, padding: Int = padding): Unit =
		preferredSize = new Dimension(size.width, size.height)
		jfxPanel.setPreferredSize(new Dimension(size.width, size.height))

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

	private def drawImageOnCanvas(): Unit =
		Platform.runLater(() => {
			val graphicsContext = canvas.getGraphicsContext2D

			graphicsContext.setFill(new javafx.scene.paint.Color(background.getRed().toDouble / 255.0, background.getBlue().toDouble / 255.0, background.getGreen().toDouble / 255.0, 1.0)) // Set your desired background color here
			graphicsContext.fillRect(0, 0, canvas.getWidth, canvas.getHeight)

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
		case class ZoomEvent(originalEvent: ScrollEvent) extends Event
		case class MousePressed(originalEvent: MouseEvent) extends Event
		case class MouseDragged(originalEvent: MouseEvent) extends Event
		case class MouseReleased(originalEvent: MouseEvent) extends Event
		case class KeyAction(event: Event) extends Event
		case class KeyPressed(originalEvent: KeyEvent) extends Event
		case class KeyReleased(originalEvent: KeyEvent) extends Event