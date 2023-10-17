package imageproject.view

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

class TopMenu extends MenuBar with Publisher:
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

		bindEventToMenuItem(openMenuItem, OpenMenuItemClicked())
		bindEventToMenuItem(newMenuItem, NewMenuItemClicked())
		bindEventToMenuItem(saveMenuItem, SaveMenuItemClicked())
		bindEventToMenuItem(exitMenuItem, ExitMenuItemClicked())

	val fileMenu = new FileMenu()
		
	val actionMenu = new Menu("Edit"):
		val undoMenuItem = new MenuItem("Undo")
		undoMenuItem.peer.setAccelerator(KeyStroke.getKeyStroke('Z', ctrlKey))
		undoMenuItem.peer.setIcon(FontIcon.of(fluentui.FluentUiFilledAL.ARROW_UNDO_24, 12, Color(0xbbbbbb)))

		val redoMenuItem = new MenuItem("Redo")
		redoMenuItem.peer.setAccelerator(KeyStroke.getKeyStroke('Y', ctrlKey))
		redoMenuItem.peer.setIcon(FontIcon.of(fluentui.FluentUiFilledAL.ARROW_REDO_24, 12, Color(0xbbbbbb)))

		contents ++= Seq(undoMenuItem, redoMenuItem)

		bindEventToMenuItem(undoMenuItem, UndoMenuItemClicked())
		bindEventToMenuItem(redoMenuItem, RedoMenuItemClicked())

	contents += fileMenu
	contents += actionMenu

	def bindEventToMenuItem(item: MenuItem, event: => Event): Unit = 
		listenTo(item)
		item.reactions += { 
			case ButtonClicked(_) => publish(event) 
		}
  	
object TopMenu:
	object Events:
		case class NewMenuItemClicked() extends Event
		case class OpenMenuItemClicked() extends Event
		case class SaveMenuItemClicked() extends Event
		case class ExitMenuItemClicked() extends Event
		case class UndoMenuItemClicked() extends Event
		case class RedoMenuItemClicked() extends Event
