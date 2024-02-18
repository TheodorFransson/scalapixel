package scalapaint.controllers

import org.kordamp.ikonli.*
import org.kordamp.ikonli.swing.FontIcon
import scalapaint.EditorWindow
import scalapaint.image.{EditorImage, ImageProcessingManager}
import scalapaint.views.TopMenu
import scalapaint.views.TopMenu.Events.*

import java.awt.{Color, Toolkit}
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JSpinner.NumberEditor
import javax.swing.{KeyStroke, SwingConstants}
import javax.swing.filechooser.{FileFilter, FileNameExtensionFilter}
import scala.swing.*
import scala.swing.Swing.*
import scala.swing.event.*

class TopMenuController(model: ImageProcessingManager, view: TopMenu) extends Reactor:
	private var openFile: Option[File] = None

	listenTo(view)
	reactions += {
		case OpenMenuItemClicked() => fileChooser()
		case NewMenuItemClicked() => createNewImage()
		case SaveMenuItemClicked() => save()
		case ExitMenuItemClicked() => EditorWindow.closeOperation()
		case UndoMenuItemClicked() => model.enqueueUndo()
		case RedoMenuItemClicked() => model.enqueueRedo()
	}
	
	private def fileChooser(): Unit =
		val chooser = new FileChooser(new File("./images/"))
		chooser.peer.setAcceptAllFileFilterUsed(false)
		chooser.peer.addChoosableFileFilter(new FileNameExtensionFilter("Images", "png", "jpg", "bmp"))
		chooser.multiSelectionEnabled = false
		if chooser.showOpenDialog(view) == FileChooser.Result.Approve then
			openFile = Some(chooser.selectedFile)
			val image = ImageIO.read(chooser.selectedFile)
			if image != null then model.setNewImage(new EditorImage(image))

	private def saveAs(): Unit =
		val chooser = new FileChooser()
		chooser.peer.setAcceptAllFileFilterUsed(false)
		chooser.peer.addChoosableFileFilter(new FileNameExtensionFilter("Images", "png", "jpg", "bmp"))
		chooser.multiSelectionEnabled = false
		if chooser.showSaveDialog(view) == FileChooser.Result.Approve then
			val file = chooser.peer.getSelectedFile()
			val name = if !file.getAbsolutePath().endsWith(".png") then file.getAbsolutePath() + ".png" else file.getAbsolutePath()
			val newFile = new File(name)
			newFile.createNewFile()
			ImageIO.write(model.getImage.buffer, "PNG", newFile)

	private def save(): Unit = 
		if openFile.isDefined then 
			val file = openFile.get
			file.getName() match
				case a if a.endsWith(".png") => ImageIO.write(model.getImage.buffer, "PNG", file)
				case b if b.endsWith(".jpg") => ImageIO.write(model.getImage.buffer, "JPEG", file)
				case _ => println("bruh")
		else
			saveAs()

	def createNewImage(): Unit =
		import Dialog.*
		val widthField = new TextField():
			text = "1400"
		val heightField = new TextField():
			text = "800"

		val panel = new FlowPanel():
			contents += new Label():
				text = "Width: "
			contents += widthField
			contents += Separator()
			contents += new Label():
				text = "Height: "
			contents += heightField

		val result = showConfirmation(EditorWindow, panel.peer, "New File", Options.OkCancel)

		val width = widthField.peer.getText().toIntOption
		val height = heightField.peer.getText().toIntOption

		if result == Result.Ok then
			val newImage = EditorImage.white(new Dimension(width.getOrElse(1400), height.getOrElse(800)))
			openFile = None
			model.setNewImage(newImage)