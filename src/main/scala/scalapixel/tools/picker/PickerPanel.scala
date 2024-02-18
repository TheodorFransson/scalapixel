package scalapixel.tools.picker

import scalapixel.tools.ToolPanel

import javax.swing.border.EmptyBorder
import scala.swing.{Alignment, Panel, TextArea, TextField}

class PickerPanel extends ToolPanel("Color picker"):
	private val description = new TextArea():
		text =
			"""Use the left mouse button to set
				|the primary color, use the right
				|mouse button to set the
				|secondary color.""".stripMargin
		editable = false
		columns = 10
		rows = 10

	contents += description
