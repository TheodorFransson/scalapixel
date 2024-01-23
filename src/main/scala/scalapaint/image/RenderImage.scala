package scalapaint.image

import scalapaint.image.EditorImage

import java.awt.image.BufferedImage
import scala.swing.{Dimension, Graphics2D, Image, Point}


class RenderImage(var editorImage: EditorImage):
  private var zoomFactor: Double = 1.0
  private val imageOrigin = new Point(0, 0)
  private var updated: Boolean = true

  private var scaledImage: BufferedImage = new BufferedImage(editorImage.width, editorImage.height, BufferedImage.TYPE_INT_RGB)
  private var graphics = scaledImage.createGraphics()

  private def size = new Dimension(editorImage.width, editorImage.height)
  private def scaledWidth = (editorImage.width * zoomFactor).toInt
  private def scaledHeight = (editorImage.height * zoomFactor).toInt

  def needsUpdate(): Boolean = updated

  def reset(referenceSize: Dimension, imageSize: Dimension = size): Unit =
    zoomFactor = 1.0
    imageOrigin.setLocation(
      (referenceSize.getWidth / 2) - (imageSize.getWidth / 2),
      (referenceSize.getHeight / 2) - (imageSize.getHeight / 2)
    )

    scaleImage()
    updated = true

  def zoom(factor: Double, target: Point, referenceSize: Dimension): Unit =
    zoomFactor *= factor

    val direction = new Point(imageOrigin.x - target.x, imageOrigin.y - target.y)
    val sign = if factor > 1 then 0.1 else -0.1

    pan((direction.x * sign).toInt, (direction.y * sign).toInt)

    scaleImage()

    updated = true

  def pan(dx: Int, dy: Int): Unit =
    imageOrigin.translate(dx, dy)
    updated = true

  def render(g: Graphics2D): Unit =
    g.drawImage(scaledImage, imageOrigin.x, imageOrigin.y, null)
    updated = false

  def updateImage(editorImage: EditorImage): Unit =
    this.editorImage = editorImage
    updatePortion(0, 0, editorImage.width, editorImage.height)

  def scaleImage(): Unit =
    scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB)
    graphics = scaledImage.createGraphics()
    graphics.drawImage(editorImage.buffer, 0, 0, scaledWidth, scaledHeight, null)

  def updatePortion(x: Int, y: Int, width: Int, height: Int): Unit =
    val scaledRectX = (x * scaledWidth)
    val scaledRectY = (y * scaledHeight)
    val scaledRectWidth = (width * zoomFactor).toInt
    val scaledRectHeight = (height * zoomFactor).toInt

    val subImage = editorImage.buffer.getSubimage(x, y, width, height)

    graphics.drawImage(subImage, scaledRectX, scaledRectY, scaledRectWidth, scaledRectHeight, null)

  def getPointOnImage(point: Point): Point =
    val adjustedX = point.x - imageOrigin.x
    val adjustedY = point.y - imageOrigin.y

    val imageX = adjustedX / zoomFactor
    val imageY = adjustedY / zoomFactor

    new Point(imageX.toInt, imageY.toInt)
