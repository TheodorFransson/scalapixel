package imageproject.controller

import imageproject.view.SideToolbar
import imageproject.model.Model
import imageproject.view.SideToolbar.Events.*

import scala.swing.Swing._
import scala.swing._
import scala.swing.event._

class SideToolbarController(model: Model, view: SideToolbar) extends Reactor:
    listenTo(view)
    reactions += {
        case ZoomButtonClicked() => ??? 
        case PenButtonClicked() => ???
        case FillButtonClicked() => ???
        case ColorButtonClicked() => ???
    }
