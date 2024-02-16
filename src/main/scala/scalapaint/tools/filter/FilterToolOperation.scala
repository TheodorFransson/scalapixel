package scalapaint.tools.filter

import scalapaint.image.ImageProcessingManager
import scalapaint.image.filters.{GaussFilter, ImageFilter}
import scalapaint.tools.ToolOperation
import scalapaint.views.CanvasPanel.Events.MousePressedCanvas

class FilterToolOperation(model: ImageProcessingManager) extends ToolOperation(model):
	var filter: ImageFilter = new GaussFilter()

	def applyFilter(): Unit = model.enqueueApply(filter)

	def setFilter(filter: ImageFilter): Unit = this.filter = filter

	def setOption(option: String): Unit = filter.setOption(option)

	override def mousePressed(event: MousePressedCanvas): Unit = applyFilter()