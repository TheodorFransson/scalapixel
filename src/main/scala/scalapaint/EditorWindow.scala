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
import scalapaint.tools.pencil.PencilPanel
import scalapaint.view.components.ColorButton

object EditorWindow extends Frame:
	title = "ScalaPaint"
	resizable = true
	
	val model = new Model()

	val topMenu = new TopMenu()
	val topMenuController = new TopMenuController(model, topMenu)

	val colorPanel = new ColorPanel()
	val colorPanelController = new ColorPanelController(colorPanel)

	val parameterPanel = new ParameterPanel()

	val sideToolbar = new SideToolbar()
	val sideToolbarController = new SideToolbarController(model, sideToolbar, parameterPanel)

	val canvasPanel = new CanvasPanel(new Dimension(1400, 800), 40)
	val canvasScrollPanel = new CanvasScrollPanel(canvasPanel)
	val canvasPanelController = new CanvasPanelController(model, canvasPanel, canvasScrollPanel)

	initGui()
	setup()

	private def initGui(): Unit =
		menuBar = topMenu

		val sideBar = new BoxPanel(Orientation.Vertical):
			contents += sideToolbar
			setBackgroundColor(peer, Colors.backgroundColorAtDepth(1))

		val lowerPanel = new TabbedPane:
			pages += new TabbedPane.Page("Color", colorPanel)

		val splitPane = new SplitPane(Orientation.Horizontal, parameterPanel, lowerPanel):
			dividerLocation = 400

		val borderPanel = new BorderPanel:
			layout(sideBar) = West
			layout(canvasScrollPanel) = Center
			layout(splitPane) = East

		contents = borderPanel

	private def setup(): Unit =
		pack()
		visible = true

		centerOnScreen()

		peer.toFront()
		canvasPanel.requestFocus()
		sideToolbar.selectDefaultTool()

		sideToolbarController.listenTo(canvasPanel)

		model.setNewImage(EditorImage.white(new Dimension(400, 400)))
	
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