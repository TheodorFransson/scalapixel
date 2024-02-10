package scalapaint.tools

import scalapaint.EditorWindow
import scalapaint.image.EditorImage
import scalapaint.model.Model
import scalapaint.view.CanvasPanel.Events.*

import java.awt.geom.GeneralPath
import java.awt.{BasicStroke, Point, geom}
import java.awt.event.MouseEvent
import scala.swing.Rectangle
class PencilTool(model: Model) extends Tool(model):
  private var path = new GeneralPath()
  private var lastPoint: Option[Point] = None
  private val pointAddInterval = -1 // milliseconds
  private var lastAddTime = System.currentTimeMillis()
  private var dragging = false


  override def process(image: EditorImage): Unit =
    val g = image.graphics
    g.setColor(EditorWindow.selectedColor)
    g.setStroke(new BasicStroke(EditorWindow.getPencilWidth().toFloat, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))
    g.draw(path)

  override def mousePressed(event: MousePressedCanvas): Unit =
    if event.originalEvent.peer.getButton == MouseEvent.BUTTON1 then
      path = new GeneralPath()
      moveTo(event.pointOnImage)
      lastPoint = Some(event.pointOnImage)
      lastAddTime = System.currentTimeMillis()
      dragging = true

  override def mouseDragged(event: MouseDraggedCanvas): Unit =
    if dragging then
      lastPoint = Some(event.pointOnImage)
      val currentTime = System.currentTimeMillis()
      if (currentTime - lastAddTime >= pointAddInterval) {
        lineTo(event.pointOnImage)
        lastAddTime = currentTime
      }
      model.enqueueProcess(this) // Immediate drawing

  override def mouseReleased(event: MouseReleasedCanvas): Unit =
    if event.originalEvent.peer.getButton == MouseEvent.BUTTON1 then
      lastPoint.foreach(point => lineTo(point))
      model.enqueueProcess(this)
      lastPoint = None
      dragging = false

  private def lineTo(point: Point): Unit =
    path.lineTo(point.x.toFloat, point.y.toFloat)

  override def undo(image: EditorImage): Unit = ???

  override def getAffectedArea(): Rectangle = ???

  private def moveTo(point: Point): Unit =
    path.moveTo(point.x.toFloat, point.y.toFloat)
