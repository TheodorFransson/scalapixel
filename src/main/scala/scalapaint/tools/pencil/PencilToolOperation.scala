package scalapaint.tools.pencil

import scalapaint.{Colors, EditorWindow}
import scalapaint.history.{HistoryEntry, SimpleHistoryEntry, TwoPartHistoryEntry}
import scalapaint.image.{EditorImage, ImageProcessor}
import scalapaint.model.Model
import scalapaint.tools.ToolOperation
import scalapaint.view.CanvasPanel.Events.*

import java.awt.event.MouseEvent
import java.awt.geom.GeneralPath
import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color, Point, geom}
import scala.collection.mutable
import scala.swing.Rectangle

class PencilToolOperation(model: Model) extends ToolOperation(model) with ImageProcessor:
  private var pencilWidth: Int = 5
  private var pencilColor: Color = Colors.getPrimaryColor()
  private var strokeCap: Int = BasicStroke.CAP_ROUND
  private var strokeJoin: Int = BasicStroke.JOIN_ROUND

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

    g.setColor(pencilColor)
    g.setStroke(new BasicStroke(pencilWidth.toFloat, strokeCap, strokeJoin))
    g.draw(path)

    if (hasSaved && !dragging) then
      historyEntry.saveFinalSnapshot(image, getExtendedPathBounds(path, image.width, image.height))

    historyEntry

  override def mousePressed(event: MousePressedCanvas): Unit =
    val mouseEvent = event.originalEvent.peer

    if !isButtonLocked && (isPrimary(mouseEvent) || isSecondary(mouseEvent)) then
      lockButton(mouseEvent.getButton)

      pencilColor = if isPrimary(mouseEvent) then Colors.getPrimaryColor() else Colors.getSecondaryColor()

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
    val mouseEvent = event.originalEvent.peer

    if sameButtonAsLocked(mouseEvent.getButton) then
      lastPoint.foreach(point => lineTo(point))
      model.enqueueApply(this)
      lastPoint = None
      dragging = false

      unlockButton()

  def setPencilWidth(width: Int): Unit =
    pencilWidth = if width > 0 then width else 5

  def setStrokeCap(value: Int): Unit = strokeCap = value

  def setStrokeJoin(value: Int): Unit = strokeJoin = value

  private def lineTo(point: Point): Unit =
    path.lineTo(point.x.toFloat, point.y.toFloat)

  private def moveTo(point: Point): Unit =
    path.moveTo(point.x.toFloat, point.y.toFloat)

  private def getExtendedPathBounds(path: GeneralPath, maxWidth: Int, maxHeight: Int): Rectangle =
    val pathBounds = path.getBounds
    val extendedPathBounds = new Rectangle(
      math.max(pathBounds.x - pencilWidth, 0),
      math.max(pathBounds.y - pencilWidth, 0),
      math.min(pathBounds.width + pencilWidth * 2, maxWidth),
      math.min(pathBounds.height + pencilWidth * 2, maxHeight)
    )

    extendedPathBounds