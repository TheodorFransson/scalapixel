package scalapaint.image

import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image

import java.awt.Point
import scala.swing.Dimension

class RenderImage(var javafxImage: Image):
  private var zoomFactor = 1.0
  private val imageOrigin = new Point(0, 0)
  private var size = new Dimension(javafxImage.getWidth.toInt, javafxImage.getHeight.toInt)
  private var updated: Boolean = true

  def needsUpdate(): Boolean = updated

  def reset(referenceSize: Dimension, imageSize: Dimension = size): Unit =
    zoomFactor = 1.0
    imageOrigin.setLocation(
      (referenceSize.getWidth / 2) - (imageSize.getWidth / 2),
      (referenceSize.getHeight / 2) - (imageSize.getHeight / 2)
    )
    updated = true

  def zoom(factor: Double, target: Point, referenceSize: Dimension): Unit =
    zoomFactor *= factor

    val direction = new Point(imageOrigin.x - target.x, imageOrigin.y - target.y)
    val sign = if factor > 1 then 0.1 else -0.1

    pan((direction.x * sign).toInt, (direction.y * sign).toInt)

    updated = true

  def pan(dx: Int, dy: Int): Unit =
    imageOrigin.translate(dx, dy)
    updated = true

  def render(gc: GraphicsContext): Unit =
    gc.drawImage(javafxImage, imageOrigin.getX, imageOrigin.getY, javafxImage.getWidth * zoomFactor, javafxImage.getHeight * zoomFactor)
    updated = false

  def updateImage(newImage: Image): Unit =
    javafxImage = newImage
    size = new Dimension(javafxImage.getWidth.toInt, javafxImage.getHeight.toInt)
    updated = true

  def getPointOnImage(point: Point): Point =
    val adjustedX = point.x - imageOrigin.x
    val adjustedY = point.y - imageOrigin.y

    val imageX = adjustedX / zoomFactor
    val imageY = adjustedY / zoomFactor

    new Point(imageX.toInt, imageY.toInt)
