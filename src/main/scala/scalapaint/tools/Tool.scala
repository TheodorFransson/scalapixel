package scalapaint.tools

import scalapaint.image.{ImageProcessingManager, ImageProcessor}

import scala.swing.{Panel, Reactor}

abstract class Tool(operation: ToolOperation, panel: Panel) extends Reactor:
  listenTo(panel)

  def getOperation(): ToolOperation = operation

  def getPanel(): Panel = panel