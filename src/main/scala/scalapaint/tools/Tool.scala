package scalapaint.tools

import scalapaint.image.ImageProcessor
import scalapaint.model.Model
import scalapaint.view.CanvasPanel.Events.*

abstract class Tool(model: Model) extends ImageProcessor:
  def mousePressed(event: MousePressedCanvas): Unit = ()

  def mouseReleased(event: MouseReleasedCanvas): Unit = ()

  def mouseDragged(event: MouseDraggedCanvas): Unit = ()

  def mouseWheelMoved(event: ZoomEvent): Unit = ()

  def keyPressed(event: KeyPressedCanvas): Unit = ()

  def keyReleased(event: KeyReleasedCanvas): Unit = ()
