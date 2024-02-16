package scalapaint.tools.picker

import scalapaint.Colors
import scalapaint.history.{HistoryEntry, SimpleHistoryEntry}
import scalapaint.image.{EditorImage, ImageProcessingManager, ImageProcessor}
import scalapaint.tools.ToolOperation
import scalapaint.views.CanvasPanel.Events.MousePressedCanvas

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