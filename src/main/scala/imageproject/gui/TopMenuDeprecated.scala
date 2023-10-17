package imageproject.gui

import imageproject.EditorWindow
import imageproject.image.*

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

class TopMenuDeprecated extends MenuBar:
	private val ctrlKey = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
	private var openFile: Option[File] = None

	contents += new Menu("File"):
		val newMenuItem = new MenuItem("New")
		newMenuItem.peer.setAccelerator(KeyStroke.getKeyStroke('N', ctrlKey))
		val openMenuItem = new MenuItem("Open...")
		openMenuItem.peer.setAccelerator(KeyStroke.getKeyStroke('O', ctrlKey))
		val saveMenuItem = new MenuItem("Save")
		saveMenuItem.peer.setAccelerator(KeyStroke.getKeyStroke('S', ctrlKey))

		val saveAsMenuItem = new MenuItem("Save As...")
		val exitMenuItem = new MenuItem("Exit")
		exitMenuItem.peer.setAccelerator(KeyStroke.getKeyStroke('Q', ctrlKey))

		contents ++= Seq(newMenuItem, openMenuItem, saveMenuItem, saveAsMenuItem, new Separator, exitMenuItem)
		listenTo(openMenuItem, newMenuItem, saveMenuItem, saveAsMenuItem, exitMenuItem)

		reactions += {
			case ButtonClicked(`openMenuItem`) => fileChooser()
			case ButtonClicked(`newMenuItem`) => createNewImage()
			case ButtonClicked(`saveMenuItem`) => save()
			case ButtonClicked(`saveAsMenuItem`) => saveAs()
			case ButtonClicked(`exitMenuItem`) => EditorWindow.close()
		}

	private def fileChooser(): Unit =
		val chooser = new FileChooser(new File("./images/"))
		chooser.peer.setAcceptAllFileFilterUsed(false)
		chooser.peer.addChoosableFileFilter(new FileNameExtensionFilter("Images", "png", "jpg", "bmp"))
		chooser.multiSelectionEnabled = false
		if chooser.showOpenDialog(this) == FileChooser.Result.Approve then
			openFile = Some(chooser.selectedFile)
			val image = ImageIO.read(chooser.selectedFile)
			if image != null then EditorWindow.setCurrentImage(new EditorImage(image))

	private def saveAs(): Unit =
		val chooser = new FileChooser()
		chooser.peer.setAcceptAllFileFilterUsed(false)
		chooser.peer.addChoosableFileFilter(new FileNameExtensionFilter("Images", "png", "jpg", "bmp"))
		chooser.multiSelectionEnabled = false
		if chooser.showSaveDialog(this) == FileChooser.Result.Approve then
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
	
	contents += new Menu("Edit"):
		val undoMenuItem = new MenuItem("Undo")
		undoMenuItem.peer.setAccelerator(KeyStroke.getKeyStroke('Z', ctrlKey))
		undoMenuItem.peer.setIcon(FontIcon.of(fluentui.FluentUiFilledAL.ARROW_UNDO_24, 12, Color(0xbbbbbb)))

		val redoMenuItem = new MenuItem("Redo")
		redoMenuItem.peer.setAccelerator(KeyStroke.getKeyStroke('Y', ctrlKey))
		redoMenuItem.peer.setIcon(FontIcon.of(fluentui.FluentUiFilledAL.ARROW_REDO_24, 12, Color(0xbbbbbb)))

		contents ++= Seq(undoMenuItem, redoMenuItem)
		listenTo(undoMenuItem, redoMenuItem)

		reactions += {
			case ButtonClicked(`undoMenuItem`) => EditorWindow.history.undo()
			case ButtonClicked(`redoMenuItem`) => EditorWindow.history.redo()
		}