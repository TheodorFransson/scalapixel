package scalapaint.view

import org.kordamp.ikonli.*
import org.kordamp.ikonli.swing.FontIcon
import scalapaint.{Colors, EditorWindow, EventBinder}
import scalapaint.tools.*

import java.awt.Color
import javax.swing.JButton
import javax.swing.border.LineBorder
import scala.collection.mutable.Buffer
import scala.swing.*
import scala.swing.ListView.*
import scala.swing.Swing.*
import scala.swing.event.*

class SideToolbar extends ToolBar with EventBinder:
    import SideToolbar.Events.*

    peer.setOrientation(Orientation.Horizontal.id)
    peer.setAlignmentX(java.awt.Component.RIGHT_ALIGNMENT)
    peer.setFloatable(false)

    private val buttonSize = 36
    private val iconSize = 24
    private val buttonColor = Color(0xf1f1f1)
    private val selectedButtonColor = Color(0xbb86fc)

    private val icons = Vector(
        FontIcon.of(fluentui.FluentUiFilledMZ.SEARCH_24, iconSize, buttonColor),
        FontIcon.of(fluentui.FluentUiFilledAL.COLOR_LINE_24, iconSize, buttonColor),
        FontIcon.of(fluentui.FluentUiFilledMZ.PAINT_BUCKET_24, iconSize, buttonColor),
    )

    private val zoom = new ToggleButton:
        icon = icons(0)
        tooltip = "Zoom"

    private val pen = new ToggleButton:
        icon = icons(1)
        tooltip = "Pencil"
        
    private val fill = new ToggleButton:
        icon = icons(2)
        tooltip = "Bucket fill"

    private val color = new Button:
        border = Swing.LineBorder(Colors.backgroundColorDP(7))
        peer.setContentAreaFilled(false)
        tooltip = "Color picker"
        setupButton(this)

        override protected def paintComponent(g: Graphics2D): Unit = 
            if peer.getModel().isRollover() then
                g.setColor(Colors.brighten(EditorWindow.selectedColor, 25))
            else 
                g.setColor(EditorWindow.selectedColor)

            g.fillRect(0, 0, peer.getWidth(), peer.getHeight())
            super.paintComponent(g)

    bindToEvent(zoom, ZoomToolActivated(), () => toggleSelection(zoom))
    bindToEvent(pen, PencilToolActivated(), () => toggleSelection(pen))
    bindToEvent(fill, FloodfillToolActivated(), () => toggleSelection(fill))
    bindToEvent(color, ColorChooserActivated())

    private val toggleButtons = Seq(zoom, pen, fill)
    private val buttons = Seq(color)

    toggleButtons.foreach(btn => setupButton(btn))

    contents ++= toggleButtons
    contents ++= buttons

    def repaintColorButton(): Unit = color.repaint()

    private def disableAll(): Unit =
        for i <- toggleButtons.indices do
            toggleButtons(i).selected = false
            icons(i).setIconColor(buttonColor)

    private def toggleSelection(btn: ToggleButton): Unit =
        disableAll()
        btn.selected = true
        icons(toggleButtons.indexOf(btn)).setIconColor(selectedButtonColor)

    private def setupButton(btn: AbstractButton): Unit =
        btn.peer.setPreferredSize(new Dimension(buttonSize, buttonSize))
        btn.peer.setMinimumSize(new Dimension(buttonSize, buttonSize))
        btn.peer.setMaximumSize(new Dimension(buttonSize, buttonSize))

object SideToolbar:
	object Events:
		case class ZoomToolActivated() extends Event
		case class PencilToolActivated() extends Event
		case class FloodfillToolActivated() extends Event
		case class ColorChooserActivated() extends Event
