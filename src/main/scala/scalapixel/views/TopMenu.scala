package scalapixel.views

import org.kordamp.ikonli.*
import org.kordamp.ikonli.swing.FontIcon
import scalapixel.{EditorWindow, EventBinder}
import scalapixel.image.*

import java.awt.{Color, Toolkit}
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JSpinner.NumberEditor
import javax.swing.{KeyStroke, SwingConstants}
import javax.swing.filechooser.{FileFilter, FileNameExtensionFilter}
import scala.swing.*
import scala.swing.Swing.*
import scala.swing.event.*

class TopMenu extends MenuBar with EventBinder:
	private val ctrlKey = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()

	import TopMenu.Events.*

	class FileMenu extends Menu("File"):
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

		bindToEvent(openMenuItem, OpenMenuItemClicked())
		bindToEvent(newMenuItem, NewMenuItemClicked())
		bindToEvent(saveMenuItem, SaveMenuItemClicked())
		bindToEvent(exitMenuItem, ExitMenuItemClicked())

	val fileMenu = new FileMenu()
		
	val actionMenu = new Menu("Edit"):
		val undoMenuItem = new MenuItem("Undo")
		undoMenuItem.peer.setAccelerator(KeyStroke.getKeyStroke('Z', ctrlKey))
		undoMenuItem.peer.setIcon(FontIcon.of(fluentui.FluentUiFilledAL.ARROW_UNDO_24, 12, Color(0xbbbbbb)))

		val redoMenuItem = new MenuItem("Redo")
		redoMenuItem.peer.setAccelerator(KeyStroke.getKeyStroke('Y', ctrlKey))
		redoMenuItem.peer.setIcon(FontIcon.of(fluentui.FluentUiFilledAL.ARROW_REDO_24, 12, Color(0xbbbbbb)))

		contents ++= Seq(undoMenuItem, redoMenuItem)

		bindToEvent(undoMenuItem, UndoMenuItemClicked())
		bindToEvent(redoMenuItem, RedoMenuItemClicked())

	contents += fileMenu
	contents += actionMenu

object TopMenu:
	object Events:
		case class NewMenuItemClicked() extends Event
		case class OpenMenuItemClicked() extends Event
		case class SaveMenuItemClicked() extends Event
		case class ExitMenuItemClicked() extends Event
		case class UndoMenuItemClicked() extends Event
		case class RedoMenuItemClicked() extends Event
