package scalapixel.tools.pencil

import scalapixel.tools.pencil.PencilPanel.Events.*
import scalapixel.tools.{Tool, ToolOperation}

import scala.swing.Panel

class PencilTool(operation: PencilToolOperation, panel: PencilPanel) extends Tool(operation, panel):
  reactions += {
    case SpinnerValueChanged(value) => operation.setPencilWidth(value)
    case CapValueChanged(value) => operation.setStrokeCap(value)
    case JoinValueChanged(value) => operation.setStrokeJoin(value)

  }

