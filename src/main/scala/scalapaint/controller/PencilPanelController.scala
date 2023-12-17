package scalapaint.controller

import scalapaint.model.Model
import scalapaint.view.PencilPanel
import scalapaint.view.PencilPanel.Events.*

import scala.swing.*
import scala.swing.Swing.*
import scala.swing.event.*

class PencilPanelController(model: Model, view: PencilPanel) extends Reactor:
    listenTo(view)
     reactions += {
        case SpinnerValueChanged(value: Int) => ??? 
    }