package scalapaint.gui

import scalapaint.*
import image.DrawnImage
import image.EditorImage

import scala.swing.Swing.*
import scala.swing.*
import scala.swing.event.*

import java.awt.{Color, Graphics2D, Point}
import java.awt.Image

class CanvasPanelDeprecated(initSize: Dimension, val padding: Int) extends Panel:
		updateSize(initSize, padding)
		background = Colors.backgroundColorDP(0)
		focusable = true

		private val dim = new Dimension(initSize.width - (2 * padding), initSize.height - (2 * padding))
		var image = EditorImage.blank(new Dimension(1, 1), Color((0 << 24) | (0 << 16) | (0 << 8) | (0)))
		var drawnImage = new DrawnImage(image.width, image.height, this)
		
		reactions += {
			case e: MousePressed =>
				if EditorWindow.selectedTool.nonEmpty then 
					EditorWindow.selectedTool.get.mousePressed(e, drawnImage.getActualPoint(e.point))
				repaint()
				requestFocusInWindow()
			case e: MouseDragged => 
				if EditorWindow.selectedTool.nonEmpty then 
					EditorWindow.selectedTool.get.mouseDragged(e, drawnImage.getActualPoint(e.point))
				if !drawnImage.hasFilter() then
					repaint()
			case e: MouseReleased => 
				if EditorWindow.selectedTool.isEmpty then 
					EditorWindow.selectedTool.get.mouseReleased(e, drawnImage.getActualPoint(e.point))
				repaint()
			case e: MouseWheelMoved => 
				if EditorWindow.selectedTool.nonEmpty then 
					EditorWindow.selectedTool.get.mouseWheelMoved(e, drawnImage.getActualPoint(e.point))
				drawnImage.zoomIn(e.rotation * -0.25, drawnImage.getActualPoint(e.point))
				repaint()
			case _: FocusLost => repaint()
		}

		listenTo(mouse.clicks, mouse.moves, keys, mouse.wheel)

		def setNewImage(newImage: EditorImage): Unit =
			EditorWindow.history.save()
			image = newImage
			repaint()

		def updateSize(size: Dimension, padding: Int): Unit = 
			preferredSize = new Dimension(size.width, size.height)
			revalidate()
			EditorWindow.pack()
			repaint()

		def updateDrawnImageSize(): Unit =
			drawnImage = new DrawnImage(size.width - (2 * padding), size.height - (2 * padding), this)

		override def paintComponent(g: Graphics2D): Unit =
			super.paintComponent(g)
			
			drawnImage.update()
			g.drawImage(drawnImage, padding, padding, null)
			