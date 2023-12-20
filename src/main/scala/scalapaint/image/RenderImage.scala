package scalapaint.image

import javafx.geometry.Point2D
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image

import java.awt.Point
import scala.swing.Dimension

class RenderImage(var javafxImage: Image):
  private var zoomFactor = 1.0
  private val imageOrigin = new Point(0, 0)
  private var updated: Boolean = true

  def needsUpdate(): Boolean = updated

  def zoom(factor: Double, target: Point, referenceSize: Dimension): Unit =
    zoomFactor *= factor

    val direction = new Point(imageOrigin.x - target.x, imageOrigin.y - target.y)
    val sign = if factor > 1 then 0.1 else -0.1

    pan((direction.x * sign).toInt, (direction.y * sign).toInt)
    /*
    val normalizedDirection = new Point2wD(
      (direction.x / referenceSize.getWidth),
      (direction.y / referenceSize.getHeight)
    )

    val sign = if factor > 1 then -1 else 1

    val multipliedDirection = new Point((direction.x * normalizedDirection.getX * sign).toInt, (direction.y * normalizedDirection.getY * sign).toInt)

    pan(multipliedDirection.x, multipliedDirection.y)
     */

    updated = true

  def pan(dx: Int, dy: Int): Unit =
    imageOrigin.translate(dx, dy)
    updated = true


  def render(gc: GraphicsContext): Unit =
    gc.drawImage(javafxImage, imageOrigin.getX, imageOrigin.getY, javafxImage.getWidth * zoomFactor, javafxImage.getHeight * zoomFactor)
    updated = false

  def updateImage(newImage: Image): Unit =
    javafxImage = newImage
    updated = true
