package scalapaint.image

import scalapaint.Colors
import scalapaint.image.EditorImage

import java.awt.image.BufferedImage
import scala.collection.mutable.HashSet
import scala.swing.{Dimension, Graphics2D, Image, Point, Rectangle}

class RenderImage(var editorImage: EditorImage):
  private var zoomFactor: Double = 1.0
  private val imageOrigin = new Point(0, 0)
  private var updated: Boolean = true

  private def size = new Dimension(editorImage.width, editorImage.height)
  private def scaledWidth = (editorImage.width * zoomFactor).toInt
  private def scaledHeight = (editorImage.height * zoomFactor).toInt

  def needsUpdate(): Boolean = updated

  def reset(reference: Dimension, imageSize: Dimension = size): Unit =
    zoomFactor = 1.0
    imageOrigin.setLocation(
      (reference.getWidth / 2) - (imageSize.getWidth / 2),
      (reference.getHeight / 2) - (imageSize.getHeight / 2)
    )

    updated = true

  def zoom(factor: Double, target: Point, reference: Dimension): Unit =
    zoomFactor *= factor

    val direction = new Point(imageOrigin.x - target.x, imageOrigin.y - target.y)
    val sign = if factor > 1 then 0.1 else -0.1

    pan((direction.x * sign).toInt, (direction.y * sign).toInt, reference)

    updated = true

  def pan(dx: Int, dy: Int, reference: Dimension): Unit =
    imageOrigin.translate(dx, dy)
    clampImageOrigin(reference)
    updated = true

  private def clampImageOrigin(reference: Dimension): Unit =
    val bounds = getBounds(reference)

    imageOrigin.x = math.max(bounds.x, math.min(imageOrigin.x, bounds.width))
    imageOrigin.y = math.max(bounds.y, math.min(imageOrigin.y, bounds.height))

  def getBounds(reference: Dimension): Rectangle =
    val minX = if (scaledWidth < reference.width) then -(scaledWidth / 2) else reference.width / 2 - scaledWidth
    val maxX = if (scaledWidth < reference.width) then reference.width - (scaledWidth / 2) else reference.width / 2

    val minY = if (scaledHeight < reference.height) then -(scaledHeight / 2) else reference.height / 2 - scaledHeight
    val maxY = if (scaledHeight < reference.height) then reference.height - (scaledHeight / 2) else reference.height / 2

    new Rectangle(minX, minY, maxX, maxY)

  def getPosition: Point = imageOrigin

  def render(g: Graphics2D, reference: Dimension): Unit =
    g.drawImage(editorImage.buffer, imageOrigin.x, imageOrigin.y, scaledWidth, scaledHeight, null)

    updated = false

  def updateImage(editorImage: EditorImage): Unit =
    this.editorImage = editorImage
    updated = true

  def getPointOnImage(point: Point): Point =
    val adjustedX = point.x - imageOrigin.x
    val adjustedY = point.y - imageOrigin.y

    val imageX = adjustedX / zoomFactor
    val imageY = adjustedY / zoomFactor

    new Point(imageX.toInt, imageY.toInt)

  def getPointOnImageInverse(point: Point): Point =
    val scaledX = point.x * zoomFactor
    val scaledY = point.y * zoomFactor

    val referenceX = scaledX + imageOrigin.x
    val referenceY = scaledY + imageOrigin.y

    new Point(referenceX.toInt, referenceY.toInt)