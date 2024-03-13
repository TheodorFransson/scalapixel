package scalapixel.tools.filter

import scalapixel.image.ImageProcessingManager
import scalapixel.image.filters.{GaussFilter, ImageFilter}
import scalapixel.tools.ToolOperation
import scalapixel.views.CanvasPanel.Events.MousePressedCanvas

class FilterToolOperation(model: ImageProcessingManager) extends ToolOperation(model):
	var filter: ImageFilter = new GaussFilter()

	def applyFilter(): Unit = model.enqueueApply(filter)

	def setFilter(filter: ImageFilter): Unit = this.filter = filter

	def setOption(option: String): Unit = filter.setOption(option)

	override def mousePressed(event: MousePressedCanvas): Unit =
		if isPrimary(event.originalEvent.peer) then applyFilter()