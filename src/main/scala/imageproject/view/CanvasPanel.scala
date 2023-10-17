package imageproject.view

import imageproject.*
import image.DrawnImage
import image.EditorImage

import scala.swing.Swing.*
import scala.swing.*
import scala.swing.event.*

import java.awt.{Color, Graphics2D, Point}
import java.awt.Image

class CanvasPanel(initSize: Dimension, val padding: Int) extends Panel:
		updateSize(initSize, padding)
		background = Colors.backgroundColorDP(0)
		focusable = true

		private val dim = new Dimension(initSize.width - (2 * padding), initSize.height - (2 * padding))

		def updateSize(size: Dimension, padding: Int): Unit = 
			preferredSize = new Dimension(size.width, size.height)
			revalidate()
			EditorWindow.pack()
			repaint()


		override def paintComponent(g: Graphics2D): Unit =
			super.paintComponent(g)
		
			