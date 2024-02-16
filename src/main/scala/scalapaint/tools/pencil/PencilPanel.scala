package scalapaint.tools.pencil

import scalapaint.tools.{ParameterGridPanel, ToolPanel}
import scalapaint.{Colors, EventBinder}

import java.awt.{BasicStroke, Color, GridBagConstraints, GridBagLayout}
import javax.swing.{JPanel, JSpinner, SpinnerNumberModel}
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

    private val joins = Map(
        "Round" -> BasicStroke.JOIN_ROUND,
        "Bevel" -> BasicStroke.JOIN_BEVEL,
        "Miter" -> BasicStroke.JOIN_MITER
    )

    private val capComboBox = new ComboBox[String](caps.keys.toList):
        selection.item = caps.keys.head

    private val joinComboBox = new ComboBox[String](joins.keys.toList):
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