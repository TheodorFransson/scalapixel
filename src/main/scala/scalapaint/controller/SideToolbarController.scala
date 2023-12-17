package scalapaint.controller

import scalapaint.model.Model
import scalapaint.view.SideToolbar
import scalapaint.view.SideToolbar.Events.*

import scala.swing.*
import scala.swing.Swing.*
import scala.swing.event.*

class SideToolbarController(model: Model, view: SideToolbar) extends Reactor:
    listenTo(view)
    reactions += {
        case ZoomButtonClicked() => ??? 
        case PenButtonClicked() => ???
        case FillButtonClicked() => ???
        case ColorButtonClicked() => ???
    }
