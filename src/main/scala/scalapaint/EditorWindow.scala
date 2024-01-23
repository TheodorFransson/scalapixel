package scalapaint

import image.*

import scala.swing.*
import BorderPanel.Position.*
import javax.swing.JPanel
import javax.swing.JColorChooser
import java.awt.{Color, Container}
import model.*
import view.*
import controller.*
import javafx.application.Platform

object EditorWindow extends Frame:
	title = "ScalaPaint"
	resizable = true
	
	val model = new Model()

	val topMenu = new TopMenu()
	val TopMenuController = new TopMenuController(model, topMenu)
	menuBar = topMenu

	val sideToolbar = new SideToolbar()
	val sideToolbarController = new SideToolbarController(model, sideToolbar)
	val pencilPanel = new PencilPanel()
	val pencilPanelController = new PencilPanelController(model, pencilPanel)
	val filterPanel = new FilterPanel()
	val filterPanelController = new FilterPanelController(model, filterPanel)
		
	private val colorChooser = new ColorChooser:
		peer.setPreviewPanel(new JPanel())

		val panels = peer.getChooserPanels
		panels.foreach(panel => if panel.getDisplayName() != "HSV" then peer.removeChooserPanel(panel))

	private val colorDialog = JColorChooser.createDialog(filterPanel.peer, "Choose color", true, colorChooser.peer, null, null)

	private val sideBar = new BoxPanel(Orientation.Vertical):
		contents += sideToolbar
		contents += new Separator:
			maximumSize = new Dimension(500, 10)
		contents += pencilPanel
		contents += new Separator:
			maximumSize = new Dimension(500, 10)
		contents += filterPanel

	setBackgroundColor(sideBar.peer, Colors.backgroundColorDP(1))

	val canvasPanel = new CanvasPanel(new Dimension(1400, 800), 40)
	val canvasPanelController = new CanvasPanelController(model, canvasPanel)
	sideToolbarController.listenTo(canvasPanel)

	var selectedColor: Color = Color.BLACK

	private val borderPanel = new BorderPanel:
		layout(sideBar) = West
		layout(canvasPanel) = Center

	contents = borderPanel

	pack()
	visible = true
	centerOnScreen()
	peer.toFront()
	canvasPanel.requestFocus()

	model.setNewImage(EditorImage.white(new Dimension(400, 400)))

	/** @return an int that represents the width from the pencilPanel. */
	def getPencilWidth(): Int = 2

	/** Repaints the canvas. */
	def repaintCanvas(): Unit = canvasPanel.repaint()

	/** Repaints the color buton to match the selected color. */
	def repaintColorButton(): Unit = sideToolbar.repaintColorButton()

	/** Shows a dialog with colorChooser
	 * @return the selected color
	 */
	def chooseColor(): Color =
		colorChooser.color = selectedColor
		colorDialog.setVisible(true)
		selectedColor = colorChooser.color
		selectedColor

	private def changeSize(dim: Dimension): Unit = 
		size = dim

	private def setBackgroundColor(parent: Container, color: Color): Unit =
		parent.setBackground(color)

		val components = parent.getComponents()
		components.foreach(c => 
			if c.isInstanceOf[Container] then 		
				if c.isInstanceOf[JPanel] then
					c.setBackground(color)
				
				setBackgroundColor(c.asInstanceOf[Container], color)
		)

	override def closeOperation(): Unit =
		canvasPanelController.dispose()
		dispose()

		val timer = new java.util.Timer()
		timer.schedule(new java.util.TimerTask {
			override def run(): Unit = {
				Platform.exit()
				System.exit(0)
			}
		}, 500)