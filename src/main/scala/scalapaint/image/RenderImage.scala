package scalapaint.image

import javafx.geometry.Point2D
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image

class RenderImage(var javafxImage: Image):
  private var zoomFactor = 1.0
  //private var center = new Point2D()
  private var updated: Boolean = true

  def needsUpdate(): Boolean = updated

  def zoom(factor: Double): Unit =
    zoomFactor *= factor
    updated = true
    // Apply zoom transformation logic

  def pan(dx: Int, dy: Int): Unit =
    updated = true

  def render(gc: GraphicsContext): Unit =
    gc.drawImage(javafxImage, 0, 0, javafxImage.getWidth * zoomFactor, javafxImage.getHeight * zoomFactor)
    updated = false

  def updateImage(newImage: Image): Unit =
    javafxImage = newImage
    updated = true
