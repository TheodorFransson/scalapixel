package scalapixel.views

import scalapixel.{Colors, EditorWindow}

import scala.swing.*
import java.awt.{Graphics2D, GridBagConstraints, GridBagLayout, Image, RenderingHints}
import javax.imageio.ImageIO
import java.io.File
import javax.swing.{BorderFactory, JTextPane, UIManager}
import javax.swing.border.EmptyBorder
import javax.swing.text.{SimpleAttributeSet, StyleConstants}
import scala.swing.BorderPanel.Position.*

class StartPanel extends Panel:
	background = Colors.backgroundColorAtDepth(0)
	focusable = false

	private var controlsImage: Option[Image] = Option.empty

	private val text =
		"""
			|Create a new image with Ctrl+N
			|Open an existing image with Ctrl+O
			|
			|Use the controls below to move and enlarge the image
			|""".stripMargin

	loadImage()

	override def paintComponent(g: Graphics2D): Unit =
		super.paintComponent(g)

		val defaultButtonFont = UIManager.getFont("Button.font")
		val largerFont = defaultButtonFont.deriveFont(16f)

		g.setFont(largerFont)
		g.setColor(new Color(0x606060))

		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
		g.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100)

		val lines = text.split("\n")
		val metrics = g.getFontMetrics(largerFont)
		val lineHeight = metrics.getHeight

		val totalTextHeight = lineHeight * lines.length
		val totalGraphicHeight = if controlsImage.isDefined then
			totalTextHeight + metrics.getDescent + controlsImage.get.getHeight(null)
		else
			totalTextHeight

		var textY = (size.height - totalGraphicHeight) / 2

		for (line <- lines) {
			val strWidth = metrics.stringWidth(line)
			g.drawString(line, (size.width - strWidth) / 2, textY + metrics.getAscent)
			textY += lineHeight
		}

		controlsImage.foreach(image => {
			val x = (size.width - image.getWidth(null)) / 2
			val y = textY + metrics.getDescent
			g.drawImage(image, x, y, null)
		})

	private def loadImage(): Unit =
		val imageUrl = getClass.getResource("/images/controls.png")
		if (imageUrl != null) then
			val image = ImageIO.read(imageUrl)
			controlsImage = Some(image.getScaledInstance(image.getWidth / 4, image.getHeight / 4, java.awt.Image.SCALE_SMOOTH))
		else
			println("Resource /images/controls.png not found.")
