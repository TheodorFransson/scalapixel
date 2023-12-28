package scalapaint.tools

import javafx.scene.input.MouseButton
import scalapaint.EditorWindow
import scalapaint.image.EditorImage
import scalapaint.model.Model
import scalapaint.view.CanvasPanel.Events.*

import java.awt.geom.GeneralPath
import java.awt.{BasicStroke, Point, geom}

class PencilTool(model: Model) extends Tool(model):
  private var path = new GeneralPath()
  private var lastPoint: Option[Point] = None
  private val pointAddInterval = 25 // milliseconds
  private var lastAddTime = System.currentTimeMillis()


  override def process(image: EditorImage): EditorImage =
    val g = image.graphics
    g.setColor(EditorWindow.selectedColor)
    g.setStroke(new BasicStroke(EditorWindow.getPencilWidth().toFloat, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))
    g.draw(path)

    image

  override def mousePressed(event: MousePressed): Unit =
    if (event.originalEvent.getButton == MouseButton.PRIMARY) then
      path = new GeneralPath()
      moveTo(event.pointOnImage)
      lastPoint = Some(event.pointOnImage)
      lastAddTime = System.currentTimeMillis()

  override def mouseDragged(event: MouseDragged): Unit =
    if (event.originalEvent.getButton == MouseButton.PRIMARY) then
      lastPoint = Some(event.pointOnImage)
      val currentTime = System.currentTimeMillis()
      if (currentTime - lastAddTime >= pointAddInterval) {
        lineTo(event.pointOnImage)
        lastAddTime = currentTime
      }
      model.enqueueProcess(this) // Immediate drawing

  override def mouseReleased(event: MouseReleased): Unit =
    if (event.originalEvent.getButton == MouseButton.PRIMARY) then
      lastPoint.foreach(point => lineTo(point))
      model.enqueueProcess(this)
      lastPoint = None

  private def lineTo(point: Point): Unit =
    path.lineTo(point.x.toFloat, point.y.toFloat)

  private def moveTo(point: Point): Unit =
    path.moveTo(point.x.toFloat, point.y.toFloat)
