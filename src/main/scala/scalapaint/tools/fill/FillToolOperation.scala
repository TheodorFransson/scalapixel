package scalapaint.tools.fill

import scalapaint.{Colors, EditorWindow}
import scalapaint.history.{HistoryEntry, SimpleHistoryEntry}
import scalapaint.image.{EditorImage, ImageProcessingManager, ImageProcessor}
import scalapaint.tools.ToolOperation
import scalapaint.views.CanvasPanel.Events.MousePressedCanvas

import java.awt.{Color, Point}
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import scala.collection.mutable
import scala.swing.Rectangle

class FillToolOperation(model: ImageProcessingManager) extends ToolOperation(model) with ImageProcessor:
  private val mousePosition = new Point(0, 0)
  private val neighbours = Vector((-1, 0), (1, 0), (0, -1), (0, 1))
  private var targetColor: Color = Colors.getPrimaryColor()

  private var tolerance = 0

  def setTolerance(value: Int): Unit = tolerance = value

  override def mousePressed(event: MousePressedCanvas): Unit =
    val mouseEvent = event.originalEvent.peer

    targetColor = if isPrimary(mouseEvent) then Colors.getPrimaryColor() else Colors.getSecondaryColor()

    if isPrimary(mouseEvent) || isSecondary(mouseEvent) then
      mousePosition.setLocation(event.pointOnImage)
      model.enqueueApply(this)

  override def process(image: EditorImage): HistoryEntry =
    val historyEntry = new SimpleHistoryEntry()
    historyEntry.saveSnapshot(image)(new Rectangle(0, 0, image.width, image.height))

    floodFill(image)
    
    historyEntry.saveResult(image)(new Rectangle(0, 0, image.width, image.height))
    historyEntry

  private val queue = mutable.Queue[Point]()

  private def floodFill(image: EditorImage): BufferedImage =
    val buffer = image.buffer

    if isInBounds(mousePosition, buffer) then
      val startColor = buffer.getRGB(mousePosition.x, mousePosition.y)

      queue += mousePosition

      while queue.nonEmpty do
        fillNeighbour(queue.dequeue, buffer, startColor, targetColor.getRGB)

    buffer

  private def fillNeighbour(point: Point, buffer: BufferedImage, startColor: Int, targetColor: Int): Unit =
    if isInBounds(point, buffer) && buffer.getRGB(point.x, point.y) != targetColor then
      buffer.setRGB(point.x, point.y, targetColor)

      neighbours.foreach(n => {
        val newPoint = new Point(point.x + n._1, point.y + n._2)
        if isInBounds(newPoint, buffer) then
          if isWithinTolerance(buffer.getRGB(newPoint.x, newPoint.y), startColor)
            && buffer.getRGB(newPoint.x, newPoint.y) != targetColor
          then queue += newPoint
      })

  private def isWithinTolerance(color: Int, referenceColor: Int): Boolean =
    val r1 = (color >> 16) & 0xFF
    val g1 = (color >> 8) & 0xFF
    val b1 = color & 0xFF

    val r2 = (referenceColor >> 16) & 0xFF
    val g2 = (referenceColor >> 8) & 0xFF
    val b2 = referenceColor & 0xFF

    val distance = math.sqrt((r2 - r1) * (r2 - r1) + (g2 - g1) * (g2 - g1) + (b2 - b1) * (b2 - b1))

    distance <= tolerance

  private def isInBounds(point: Point, buffer: BufferedImage): Boolean =
    point.y < buffer.getHeight() && point.y >= 0 && point.x < buffer.getWidth() && point.x >= 0


