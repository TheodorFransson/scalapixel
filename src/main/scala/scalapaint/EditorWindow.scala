package scalapaint

import image.*

import scala.swing.*
import BorderPanel.Position.*
import javax.swing.JPanel
import javax.swing.JColorChooser
import java.awt.{Color, Container}
import views.*
import controllers.*
import scalapaint.tools.pencil.PencilPanel
import scalapaint.views.components.ColorButton

import scala.swing.Swing.EmptyBorder

object EditorWindow extends Frame:
	title = "ScalaPaint"
	resizable = true

	preferredSize = new Dimension(1600, 900)
	
	val model = new ImageProcessingManager()

	val topMenu = new TopMenu()
	val topMenuController = new TopMenuController(model, topMenu)

	val colorPanel = new ColorPanel()
	val colorPanelController = new ColorPanelController(colorPanel)

	val toolTabPanel = new ToolTabPanel()

	val sideToolbar = new SideToolbar()
	val sideToolbarController = new SideToolbarController(model, sideToolbar, toolTabPanel)

	val canvasPanel = new CanvasPanel()
	val startPanel = new StartPanel()
	val navigablePanel = new NavigablePanel(startPanel, 100, 2, 1, 30)

	val canvasPanelController = new CanvasPanelController(model, canvasPanel, navigablePanel)
	val startPanelController = new StartPanelController(model, startPanel)

	initGui()
	setup()

	def showStartPanel(): Unit =
		navigablePanel.setInnerPanel(startPanel)
		navigablePanel.navigationEnabled(false)

	def showCanvasPanel(): Unit =
		navigablePanel.setInnerPanel(canvasPanel)
		navigablePanel.navigationEnabled(true)

		canvasPanel.preferredSize = startPanel.size
		preferredSize = size

		pack()
		repaint()

		canvasPanel.requestFocus()

	private def initGui(): Unit =
		menuBar = topMenu

		val sideBar = new BoxPanel(Orientation.Vertical):
			contents += sideToolbar

		val colorTabPanel = new TabbedPane:
			pages += new TabbedPane.Page("Color", colorPanel)
			peer.putClientProperty("JTabbedPane.tabType", "card")

		val splitPane = new SplitPane(Orientation.Horizontal, toolTabPanel, colorTabPanel):
			dividerLocation = 200
			border = EmptyBorder(5, 5, 5, 5)
			preferredSize = new Dimension(200, preferredSize.height)

		val borderPanel = new BorderPanel:
			layout(sideBar) = West
			layout(navigablePanel) = Center
			layout(splitPane) = East

		contents = borderPanel

	private def setup(): Unit =
		pack()
		visible = true

		centerOnScreen()

		peer.toFront()
		sideToolbar.selectDefaultTool()

		sideToolbarController.listenTo(canvasPanel)

		showStartPanel()

	override def closeOperation(): Unit =
		canvasPanelController.dispose()
		dispose()

		val timer = new java.util.Timer()
		timer.schedule(new java.util.TimerTask {
			override def run(): Unit = {
				System.exit(0)
			}
		}, 500)