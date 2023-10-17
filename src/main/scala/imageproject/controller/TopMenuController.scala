package imageproject.controller

import imageproject.view.TopMenu
import imageproject.model.Model

import scala.swing.Swing._
import scala.swing._
import scala.swing.event._

import java.awt.Toolkit
import java.awt.Color
import javax.swing.KeyStroke
import javax.swing.SwingConstants

import org.kordamp.ikonli.*
import org.kordamp.ikonli.swing.FontIcon
import javax.swing.filechooser.FileFilter
import java.io.File
import javax.swing.filechooser.FileNameExtensionFilter
import javax.imageio.ImageIO
import javax.swing.JSpinner.NumberEditor

import TopMenu.Events.*

class TopMenuController(model: Model, view: TopMenu) extends Reactor:
	private var openFile: Option[File] = None

	listenTo(view)
	reactions += {
		case OpenMenuItemClicked() => fileChooser()
		case NewMenuItemClicked() => createNewImage()
		case SaveMenuItemClicked() => save()
		case ExitMenuItemClicked() => 
		case UndoMenuItemClicked() => 
		case RedoMenuItemClicked() => 
	}
	
	private def fileChooser(): Unit =
		val chooser = new FileChooser(new File("./images/"))
		chooser.peer.setAcceptAllFileFilterUsed(false)
		chooser.peer.addChoosableFileFilter(new FileNameExtensionFilter("Images", "png", "jpg", "bmp"))
		chooser.multiSelectionEnabled = false
		if chooser.showOpenDialog(view) == FileChooser.Result.Approve then
			openFile = Some(chooser.selectedFile)
		val image = ImageIO.read(chooser.selectedFile)
		if image != null then EditorWindow.setCurrentImage(new EditorImage(image))

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
			ImageIO.write(EditorWindow.getCurrentImage().buffer, "PNG", newFile)

	private def save(): Unit = 
		if openFile.isDefined then 
			val file = openFile.get
			file.getName() match
				case a if a.endsWith(".png") => ImageIO.write(EditorWindow.getCurrentImage().buffer, "PNG", file)
				case b if b.endsWith(".jpg") => ImageIO.write(EditorWindow.getCurrentImage().buffer, "JPEG", file)
				case _ => println("bruh")
		else
			saveAs()

	def createNewImage(): Option[EditorImage] =
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

		val values = Seq("1920", "1080")
		val result = showConfirmation(EditorWindow, panel.peer, "New File", Options.OkCancel)

		val width = widthField.peer.getText().toIntOption
		val height = heightField.peer.getText().toIntOption

		if result == Result.Ok then
			val newImage = EditorImage.white(new Dimension(width.getOrElse(1400), height.getOrElse(800)))
			EditorWindow.setCurrentImage(newImage)
			Some(newImage)
		else
			None