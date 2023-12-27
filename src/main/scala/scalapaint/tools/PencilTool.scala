package scalapaint.tools

import scalapaint.EditorWindow
import scalapaint.image.EditorImage
import scalapaint.model.Model
import scalapaint.view.CanvasPanel.Events.*


import javafx.scene.input.{MouseButton}

import java.awt.geom.GeneralPath
import java.awt.{BasicStroke, Point, geom}

class PencilTool(model: Model) extends Tool(model):
  private var path = new GeneralPath()

  override def process(image: EditorImage): EditorImage =
    val g = image.buffer.createGraphics()
    g.setColor(EditorWindow.selectedColor)
    g.setStroke(new BasicStroke(EditorWindow.getPencilWidth().toFloat, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))
    g.draw(path)

    image

  override def mousePressed(event: MousePressed): Unit =
    if (event.originalEvent.getButton == MouseButton.PRIMARY) then
      path = new GeneralPath()
      moveTo(event.pointOnImage)

  override def mouseDragged(event: MouseDragged): Unit =
    if (event.originalEvent.getButton == MouseButton.PRIMARY) then
      lineTo(event.pointOnImage)
      model.enqueueProcess(this)

  override def mouseReleased(event: MouseReleased): Unit =
    if (event.originalEvent.getButton == MouseButton.PRIMARY) then
      lineTo(event.pointOnImage)
      path = new GeneralPath()
      model.enqueueProcess(this)

  private def lineTo(point: Point): Unit = path.lineTo(point.x.toFloat, point.y.toFloat)

  private def moveTo(point: Point): Unit = path.moveTo(point.x.toFloat, point.y.toFloat)