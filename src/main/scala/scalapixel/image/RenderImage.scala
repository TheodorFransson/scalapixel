package scalapixel.image

import scalapixel.Colors
import scalapixel.image.EditorImage

import java.awt.image.BufferedImage
import scala.collection.mutable.HashSet
import scala.swing.{Dimension, Graphics2D, Image, Point, Rectangle}

class RenderImage(var editorImage: EditorImage):
  private var zoomFactor: Double = 1.0
  private val position = new Point(0, 0)

  private def size = new Dimension(editorImage.width, editorImage.height)
  private def scaledWidth = (editorImage.width * zoomFactor).toInt
  private def scaledHeight = (editorImage.height * zoomFactor).toInt

  def reset(imageSize: Dimension = size)(using reference: Dimension): Unit =
    zoomFactor = 1.0
    position.setLocation(
      (reference.getWidth / 2) - (imageSize.getWidth / 2),
      (reference.getHeight / 2) - (imageSize.getHeight / 2)
    )

  def zoom(factor: Double, target: Point)(using reference: Dimension): Unit =
    val oldDimensions = new Dimension(scaledWidth, scaledHeight)

    val change = if zoomFactor >= 3 then (factor - 1) * 5 else (factor - 1)
    zoomFactor = math.min(math.max(zoomFactor + change, 0.1), 10)

    val newDimensions = new Dimension(scaledWidth, scaledHeight)

    val widthDifference = newDimensions.width - oldDimensions.width
    val heightDifference = newDimensions.height - oldDimensions.height

    val dx = ((target.x - position.x).toDouble / oldDimensions.width * widthDifference).toInt
    val dy = ((target.y - position.y).toDouble / oldDimensions.height * heightDifference).toInt

    pan(-dx, -dy)

  def zoomAbsolute(zoom: Int)(using reference: Dimension): Unit =
    val oldDimensions = new Dimension(scaledWidth, scaledHeight)

    zoomFactor = zoom.toDouble / 10

    val newDimensions = new Dimension(scaledWidth, scaledHeight)

    val direction = new Point(
      (oldDimensions.width - newDimensions.width) / 2,
      (oldDimensions.height - newDimensions.height) / 2
    )

    pan(direction.x, direction.y)

  def pan(dx: Int, dy: Int)(using reference: Dimension): Unit =
    position.translate(dx, dy)
    clampImageOrigin

  def getBounds(using reference: Dimension): Rectangle =
    val minX = if (scaledWidth < reference.width) then -(scaledWidth / 2) else reference.width / 2 - scaledWidth
    val maxX = if (scaledWidth < reference.width) then reference.width - (scaledWidth / 2) else reference.width / 2

    val minY = if (scaledHeight < reference.height) then -(scaledHeight / 2) else reference.height / 2 - scaledHeight
    val maxY = if (scaledHeight < reference.height) then reference.height - (scaledHeight / 2) else reference.height / 2

    new Rectangle(minX, minY, maxX, maxY)

  def getPosition: Point = position

  def getZoomFactor: Double = zoomFactor

  def render(g: Graphics2D)(using reference: Dimension): Unit =
    g.drawImage(editorImage.buffer, position.x, position.y, scaledWidth, scaledHeight, null)

  def updateImage(editorImage: EditorImage): Unit =
    this.editorImage = editorImage

  def getPointOnImage(point: Point): Point =
    val adjustedX = point.x - position.x
    val adjustedY = point.y - position.y

    val imageX = adjustedX / zoomFactor
    val imageY = adjustedY / zoomFactor

    new Point(imageX.toInt, imageY.toInt)

  private def clampImageOrigin(using reference: Dimension): Unit =
    val bounds = getBounds

    position.x = math.max(bounds.x, math.min(position.x, bounds.width))
    position.y = math.max(bounds.y, math.min(position.y, bounds.height))