package scalapaint.tools

import scalapaint.image.{ImageProcessingManager, ImageProcessor}

import scala.swing.{Panel, Reactor}

abstract class Tool(operation: ToolOperation, panel: ToolPanel) extends Reactor:
  listenTo(panel)

  def getOperation(): ToolOperation = operation

  def getPanel(): ToolPanel = panel