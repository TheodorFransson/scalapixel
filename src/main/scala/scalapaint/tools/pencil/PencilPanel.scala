package scalapaint.tools.pencil

import scalapaint.EventBinder

import java.awt.{BasicStroke, Color}
import javax.swing.{JSpinner, SpinnerNumberModel}
import scala.swing.*
import scala.swing.BorderPanel.Position.*
import scala.swing.Swing.*
import scala.swing.event.*

class PencilPanel extends BoxPanel(Orientation.Vertical) with EventBinder:
    import PencilPanel.Events.*

    private val model = new SpinnerNumberModel(5, 1, 100, 1)
    private val spinner = new JSpinner(model)
    private val spinnerWrap = Component.wrap(spinner)
    spinnerWrap.maximumSize = spinner.getPreferredSize()

    private val widthPanel = new BoxPanel(Orientation.Horizontal):
        contents += new Label("Width")
        contents += HGlue
        contents += spinnerWrap

    private val caps: Array[Int] = Array(
        BasicStroke.CAP_ROUND,
        BasicStroke.CAP_BUTT,
        BasicStroke.CAP_SQUARE
    )

    private val joins: Array[Int] = Array(
        BasicStroke.JOIN_ROUND,
        BasicStroke.JOIN_BEVEL,
        BasicStroke.JOIN_MITER
    )

    private val capComboBox = new ComboBox[Int](caps):
        selection.item = caps(0)
        maximumSize = preferredSize

    private val joinComboBox = new ComboBox[Int](joins):
        selection.item = joins(0)
        maximumSize = preferredSize

    private val capComboPanel = new BoxPanel(Orientation.Horizontal):
        contents += new Label("Cap")
        contents += HGlue
        contents += capComboBox

    private val joinComboPanel = new BoxPanel(Orientation.Horizontal):
        contents += new Label("Join")
        contents += HGlue
        contents += joinComboBox

    contents += VStrut(10)
    contents += widthPanel
    contents += VStrut(5)
    contents += capComboPanel
    contents += VStrut(5)
    contents += joinComboPanel
    contents += VGlue

    bindToSpinnerChangeEvent(spinner, SpinnerValueChanged.apply, model.getNumber)
    bindToValueChangeEvent(capComboBox.selection, CapValueChanged.apply, () => capComboBox.selection.item)
    bindToValueChangeEvent(joinComboBox.selection, JoinValueChanged.apply, () => joinComboBox.selection.item)

object PencilPanel:
    object Events:
        case class SpinnerValueChanged(newValue: Int) extends Event
        case class CapValueChanged(newValue: Int) extends Event
        case class JoinValueChanged(newValue: Int) extends Event