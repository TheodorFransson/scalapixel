package scalapixel.tools.picker

import scalapixel.Colors
import scalapixel.history.{HistoryEntry, SimpleHistoryEntry}
import scalapixel.image.{EditorImage, ImageProcessingManager, ImageProcessor}
import scalapixel.tools.ToolOperation
import scalapixel.views.CanvasPanel.Events.MousePressedCanvas

import scala.swing.{Color, Point}

class PickerToolOperation(model: ImageProcessingManager) extends ToolOperation(model) with ImageProcessor:
	var primary = false
	var point: Option[Point] = Option.empty

	override def process(image: EditorImage): HistoryEntry =
		if (point.nonEmpty) then

			val color = new Color(image.buffer.getRGB(point.get.x, point.get.y))

			if primary then Colors.setPrimaryColor(color) else Colors.setSecondaryColor(color)

		new SimpleHistoryEntry()

	override def mousePressed(event: MousePressedCanvas): Unit =
		val mouseEvent = event.originalEvent.peer
		primary = isPrimary(mouseEvent)

		if (isPrimary(mouseEvent) || isSecondary(mouseEvent)) then
			point = Option(event.pointOnImage)
			model.enqueueApply(this)