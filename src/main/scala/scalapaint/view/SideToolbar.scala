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

    peer.setOrientation(Orientation.Vertical.id)
    peer.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT)
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

    bindToEvent(zoom, ZoomToolActivated(), () => toggleSelection(zoom))
    bindToEvent(pen, PencilToolActivated(), () => toggleSelection(pen))
    bindToEvent(fill, FloodfillToolActivated(), () => toggleSelection(fill))

    private val toggleButtons = Seq(zoom, pen, fill)

    toggleButtons.foreach(btn => setupButton(btn))

    contents ++= toggleButtons

    def selectDefaultTool(): Unit =
        pen.publish(new SelectionChanged(pen))

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
