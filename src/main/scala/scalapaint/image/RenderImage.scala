package scalapaint.image

import javafx.geometry.Point2D
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image

import java.awt.Point

class RenderImage(var javafxImage: Image):
  private var zoomFactor = 1.0
  private val imageOrigin = new Point(0, 0)
  private var updated: Boolean = true

  def needsUpdate(): Boolean = updated

  def zoom(factor: Double): Unit =
    zoomFactor *= factor
    updated = true
    // Apply zoom transformation logic

  def pan(dx: Int, dy: Int): Unit =
    imageOrigin.translate(dx, dy)
    updated = true

  def render(gc: GraphicsContext): Unit =
    gc.drawImage(javafxImage, imageOrigin.getX, imageOrigin.getY, javafxImage.getWidth * zoomFactor, javafxImage.getHeight * zoomFactor)
    updated = false

  def updateImage(newImage: Image): Unit =
    javafxImage = newImage
    updated = true
