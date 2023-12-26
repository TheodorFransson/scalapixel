package scalapaint.tools

import scalapaint.image.ImageProcessor
import scalapaint.model.Model
import scalapaint.view.CanvasPanel.Events.*

abstract class Tool(model: Model) extends ImageProcessor:
  def mousePressed(event: MousePressed): Unit = ()

  def mouseReleased(event: MouseReleased): Unit = ()

  def mouseDragged(event: MouseDragged): Unit = ()

  def mouseWheelMoved(event: ZoomEvent): Unit = ()

  def keyPressed(event: KeyPressed): Unit = ()

  def keyReleased(event: KeyReleased): Unit = ()
