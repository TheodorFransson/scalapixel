package imageproject

import tools.Tool
import gui.*
import image.*

import scala.swing.*
import scala.swing.event.*
import BorderPanel.Position.*
import javax.swing.JPanel
import javax.swing.JColorChooser
import java.awt.{Color, BorderLayout, Container}

/** The window of the application. */
object EditorWindow extends Frame:
	title = "Photostore"
	resizable = true
	
	private val topMenu = new TopMenu()
	menuBar = topMenu
	private val sideToolBar = new SideToolBar()
	private val pencilPanel = new PencilPanel()
	private val filterPanel = new FilterPanel()
		
	private val colorChooser = new ColorChooser:
		peer.setPreviewPanel(new JPanel())

		val panels = peer.getChooserPanels
		panels.foreach(panel => if panel.getDisplayName() != "HSV" then peer.removeChooserPanel(panel))

	private val colorDialog = JColorChooser.createDialog(filterPanel.peer, "Choose color", true, colorChooser.peer, null, null)

	private val sideBar = new BoxPanel(Orientation.Vertical):
		contents += sideToolBar
		contents += new Separator:
			maximumSize = new Dimension(500, 10)
		contents += pencilPanel
		contents += new Separator:
			maximumSize = new Dimension(500, 10)
		contents += filterPanel

	setBackgroundColor(sideBar.peer, Colors.backgroundColorDP(1))

	private val canvas = new CanvasPanel(new Dimension(1400, 800), 40)

	var selectedTool: Option[Tool] = None
	var selectedColor: Color = Color.BLACK
	val history = new History()

	private val borderPanel = new BorderPanel:
		layout(sideBar) = West
		layout(canvas) = Center

	contents = borderPanel

	pack()
	visible = true
	centerOnScreen()
	peer.toFront()
	peer.requestFocusInWindow()

	reactions += {
		case e: UIElementResized => canvas.updateDrawnImageSize()
	}

	listenTo(this)

	/** @return the underlying image used by the canvas. */
	def getCurrentImage(): EditorImage = canvas.image

	/** @return the image drawn by the canvas. */
	def getDrawnImage(): DrawnImage = canvas.drawnImage

	/** Calls setNewImage on canvas to set a new underlying image
	 * @param image a new EditorImage to use
	 */
	def setCurrentImage(image: EditorImage): Unit = canvas.setNewImage(image)

	/** Sets the underlying image used by the canvas directly, thus not saving the last image to history
	 * @param image a new EditorImage to use
	 */
	def setCurrentImageWithoutSaving(image: EditorImage): Unit = canvas.image = image

	/** Calls createNewImage on topMenu to create a new image by allowing user to specify dimensions
	 * @return an option EditorImage
	 */
	def createNewImage(): Option[EditorImage] = topMenu.createNewImage()

	/** @return an int that represents the width from the pencilPanel. */
	def getPencilWidth(): Int = pencilPanel.getWidth()

	/** @return args from filterPanel as Option. */
	def getFilterArgs(): Option[Double] = filterPanel.getArgs()

	/** Repaints the canvas. */
	def repaintCanvas(): Unit = canvas.repaint()

	/** Repaints the color buton to match the selected color. */
	def repaintColorButton(): Unit = sideToolBar.repaintColorButton()

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

	override def closeOperation(): Unit = dispose()