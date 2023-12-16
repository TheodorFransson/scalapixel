package imageproject.controller

import imageproject.model.Model
import imageproject.view.PencilPanel
import imageproject.view.PencilPanel.Events.*

import scala.swing.Swing._
import scala.swing._
import scala.swing.event._

class PencilPanelController(model: Model, view: PencilPanel) extends Reactor:
    listenTo(view)
     reactions += {
        case SpinnerValueChanged(value: Int) => ??? 
    }