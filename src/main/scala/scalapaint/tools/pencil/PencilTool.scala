package scalapaint.tools.pencil

import scalapaint.tools.pencil.PencilPanel.Events.SpinnerValueChanged
import scalapaint.tools.{Tool, ToolOperation}

import scala.swing.Panel

class PencilTool(operation: PencilToolOperation, panel: PencilPanel) extends Tool(operation, panel):
  reactions += {
    case SpinnerValueChanged(value: Int) => 
      operation.setPencilWidth(value)
  }

