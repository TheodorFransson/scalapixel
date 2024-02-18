package scalapixel.tools.pencil

import scalapixel.tools.ToolPanel
import scalapixel.tools.components.{ParameterGridPanel, TooltipComboBox}
import scalapixel.{Colors, EventBinder}

import java.awt.{BasicStroke, Color, GridBagConstraints, GridBagLayout}
import javax.swing.{JPanel, JSpinner, JToolTip, SpinnerNumberModel}
import scala.swing.*
import scala.swing.BorderPanel.Position.*
import scala.swing.Swing.*
import scala.swing.event.*

class PencilPanel extends ToolPanel("Pencil") with EventBinder:
    import PencilPanel.Events.*

    private val model = new SpinnerNumberModel(5, 1, 100, 1)
    private val spinner = new JSpinner(model)
    private val spinnerWrap = Component.wrap(spinner)

    private val caps = Map(
        "Round" -> BasicStroke.CAP_ROUND,
        "Cutoff" -> BasicStroke.CAP_BUTT,
        "Square" -> BasicStroke.CAP_SQUARE
    )

    private val capTooltips = Seq(
        "<html>Creates a rounded end<br>to lines and paths.</html>",
        "<html>Ends the line or path exactly<br>at its endpoint, with no extension.</html>",
        "<html>Extends the line or path beyond<br>the endpoint by half the pen's width,<br>with a squared-off end.</html>"
    )

    private val joins = Map(
        "Round" -> BasicStroke.JOIN_ROUND,
        "Bevel" -> BasicStroke.JOIN_BEVEL,
        "Miter" -> BasicStroke.JOIN_MITER
    )

    private val joinTooltips = Seq(
        "<html>Creates a rounded corner<br>where lines or path segments meet.</html>",
        "<html>Produces a flattened corner by connecting<br>the outer corners of the stroke.</html>",
        "<html>Extends the outer edges of the strokes<br>until they meet at an angle,<br>creating a sharp corner.</html>"
    )

    private val capComboBox = new TooltipComboBox[String](caps.keys.toList, capTooltips):
        selection.item = caps.keys.head

    private val joinComboBox = new TooltipComboBox[String](joins.keys.toList, joinTooltips):
        selection.item = joins.keys.head

    val components = Seq(
        ("Width", spinnerWrap),
        ("Cap", capComboBox),
        ("Join", joinComboBox),
    )

    private val gridPanel = new ParameterGridPanel(components)

    contents += gridPanel

    bindToSpinnerChangeEvent(spinner, SpinnerValueChanged.apply, model.getNumber)
    bindToValueChangeEvent(capComboBox.selection, CapValueChanged.apply, () => caps(capComboBox.selection.item))
    bindToValueChangeEvent(joinComboBox.selection, JoinValueChanged.apply, () => joins(joinComboBox.selection.item))

object PencilPanel:
    object Events:
        case class SpinnerValueChanged(newValue: Int) extends Event
        case class CapValueChanged(newValue: Int) extends Event
        case class JoinValueChanged(newValue: Int) extends Event