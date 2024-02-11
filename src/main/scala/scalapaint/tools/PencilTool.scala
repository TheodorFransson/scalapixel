package scalapaint.tools

import scalapaint.EditorWindow
import scalapaint.history.{HistoryEntry, SimpleHistoryEntry, TwoPartHistoryEntry}
import scalapaint.image.EditorImage
import scalapaint.model.Model
import scalapaint.view.CanvasPanel.Events.*

import java.awt.geom.GeneralPath
import java.awt.{BasicStroke, Point, geom}
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import scala.swing.Rectangle
import scala.collection.mutable

class PencilTool(model: Model) extends Tool(model):
  private var path = new GeneralPath()
  private var lastPoint: Option[Point] = None
  private val pointAddInterval = 5
  private var lastAddTime = System.currentTimeMillis()
  private var dragging = false
  private var hasSaved = false

  override def process(image: EditorImage): HistoryEntry =
    val g = image.graphics

    val historyEntry = new TwoPartHistoryEntry()

    if (!hasSaved) then
      historyEntry.savePreSnapshot(image)
      hasSaved = true
    else if (!dragging) then
      historyEntry.saveFinalSnapshot(image, getExtendedPathBounds(path, image.width, image.height))

    g.setColor(EditorWindow.selectedColor)
    g.setStroke(new BasicStroke(EditorWindow.getPencilWidth().toFloat, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))
    g.draw(path)

    historyEntry

  override def mousePressed(event: MousePressedCanvas): Unit =
    if event.originalEvent.peer.getButton == MouseEvent.BUTTON1 then
      path = new GeneralPath()
      moveTo(event.pointOnImage)
      lastPoint = Some(event.pointOnImage)
      lastAddTime = System.currentTimeMillis()
      dragging = true
      hasSaved = false

  override def mouseDragged(event: MouseDraggedCanvas): Unit =
    if dragging then
      lastPoint = Some(event.pointOnImage)
      val currentTime = System.currentTimeMillis()
      if (currentTime - lastAddTime >= pointAddInterval) {
        lineTo(event.pointOnImage)
        lastAddTime = currentTime
      }
      model.enqueueApply(this)

  override def mouseReleased(event: MouseReleasedCanvas): Unit =
    if event.originalEvent.peer.getButton == MouseEvent.BUTTON1 then
      lastPoint.foreach(point => lineTo(point))
      model.enqueueApply(this)
      lastPoint = None
      dragging = false


  private def lineTo(point: Point): Unit =
    path.lineTo(point.x.toFloat, point.y.toFloat)

  private def moveTo(point: Point): Unit =
    path.moveTo(point.x.toFloat, point.y.toFloat)

  private def getExtendedPathBounds(path: GeneralPath, maxWidth: Int, maxHeight: Int): Rectangle =
    val pathBounds = path.getBounds
    val pencilWidth = EditorWindow.getPencilWidth()
    val extendedPathBounds = new Rectangle(
      math.max(pathBounds.x - pencilWidth, 0),
      math.max(pathBounds.y - pencilWidth, 0),
      math.min(pathBounds.width + pencilWidth * 2, maxWidth),
      math.min(pathBounds.height + pencilWidth * 2, maxHeight)
    )

    extendedPathBounds