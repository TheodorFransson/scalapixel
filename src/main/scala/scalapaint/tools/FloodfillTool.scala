package scalapaint.tools
import scalapaint.EditorWindow
import scalapaint.history.{HistoryEntry, SimpleHistoryEntry}
import scalapaint.image.EditorImage
import scalapaint.model.Model
import scalapaint.view.CanvasPanel.Events.*

import java.awt.Point
import java.awt.image.BufferedImage
import scala.collection.mutable
import scala.collection.mutable.Queue
import scala.swing.Rectangle

class FloodfillTool(model: Model) extends Tool(model):
  private val mousePosition = new Point(0, 0)
  private val neighbours = Vector((-1, 0), (1, 0), (0, -1), (0, 1))

  override def mousePressed(event: MousePressedCanvas): Unit =
    mousePosition.setLocation(event.pointOnImage)
    model.enqueueApply(this)

  override def process(image: EditorImage): HistoryEntry =
    val historyEntry = new SimpleHistoryEntry()
    historyEntry.saveSnapshot(image)(new Rectangle(0, 0, image.width, image.height))

    floodFill(image)

    historyEntry

  private val queue = mutable.Queue[Point]()

  private def floodFill(image: EditorImage): BufferedImage =
    val buffer = image.buffer

    if isInBounds(mousePosition, buffer) then
      val startColor = buffer.getRGB(mousePosition.x, mousePosition.y)
      val targetColor = EditorWindow.selectedColor.getRGB

      queue += mousePosition

      while queue.nonEmpty do
        fillNeighbour(queue.dequeue, buffer, startColor, targetColor)

    buffer

  private def fillNeighbour(point: Point, buffer: BufferedImage, startColor: Int, targetColor: Int): Unit =
    if isInBounds(point, buffer) && buffer.getRGB(point.x, point.y) != targetColor then
      buffer.setRGB(point.x, point.y, targetColor)

      neighbours.foreach(n => {
        val newPoint = new Point(point.x + n._1, point.y + n._2)
        if isInBounds(newPoint, buffer) then
          if buffer.getRGB(newPoint.x, newPoint.y) == startColor && buffer.getRGB(newPoint.x, newPoint.y) != targetColor then queue += newPoint
      })

  private def isInBounds(point: Point, buffer: BufferedImage): Boolean = point.y < buffer.getHeight() && point.y >= 0 && point.x < buffer.getWidth() && point.x >= 0
