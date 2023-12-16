package imageproject.controller

import imageproject.model.Model
import imageproject.view.CanvasPanel

import scala.swing.Swing._
import scala.swing._
import scala.swing.event._

class CanvasPanelController(model: Model, view: CanvasPanel) extends Reactor:
    listenTo(view)
