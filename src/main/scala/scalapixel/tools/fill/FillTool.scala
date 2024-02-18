package scalapixel.tools.fill

import scalapixel.tools.Tool
import scalapixel.tools.fill.FillPanel.Events.SliderValueChanged

class FillTool(operation: FillToolOperation, panel: FillPanel) extends Tool(operation, panel):
  reactions += {
    case SliderValueChanged(value) => operation.setTolerance(value)
  }