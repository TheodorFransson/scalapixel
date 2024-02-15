package scalapaint.tools.filter

import scalapaint.tools.Tool
import FilterPanel.Events.*

class FilterTool(operation: FilterToolOperation, panel: FilterPanel) extends Tool(operation, panel):
	reactions += {
		case FilterSelectionChanged(selection) =>
			operation.setFilter(selection)
			operation.setOption(panel.getParameterValue)
		case ApplyFilter() => operation.applyFilter()
		case FilterParameterChanged(parameter) => operation.setOption(parameter)
	}

