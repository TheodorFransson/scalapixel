package scalapaint.tools.fill

import scalapaint.tools.Tool
import scalapaint.tools.fill.FillPanel.Events.SliderValueChanged

class FillTool(operation: FillToolOperation, panel: FillPanel) extends Tool(operation, panel):
  reactions += {
    case SliderValueChanged(value) => operation.setTolerance(value)
  }