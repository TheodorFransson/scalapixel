package scalapaint.views

import scalapaint.{Colors, EditorWindow}

import scala.swing._
import java.awt.{Graphics2D, Image}
import javax.imageio.ImageIO
import java.io.File

class StartPanel extends MainPanel:
	private val controlsImagePath = "./images/controls.png"
	private var controlsImage: Image = _

	try {
		controlsImage = ImageIO.read(new File(controlsImagePath))
		controlsImage = controlsImage.getScaledInstance(controlsImage.getWidth(null) / 4, controlsImage.getHeight(null) / 4, java.awt.Image.SCALE_SMOOTH)
	} catch {
		case e: Exception =>
			e.printStackTrace()
			controlsImage = null
	}

	override def paintComponent(g: Graphics2D): Unit =
		super.paintComponent(g)

		if (controlsImage != null) {

			g.drawImage(controlsImage, (size.width - controlsImage.getWidth(null))  / 2, (size.height - controlsImage.getHeight(null))  / 2, null)
		}