package scalapaint.controller

import scalapaint.model.Model
import scalapaint.view.CanvasPanel

import scala.swing.*
import scala.swing.Swing.*
import scala.swing.event.*

class CanvasPanelController(model: Model, view: CanvasPanel) extends Reactor:
    listenTo(view)
