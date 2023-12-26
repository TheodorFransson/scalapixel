package scalapaint.tools

import scalapaint.image.ImageProcessor
import scalapaint.view.CanvasPanel.Events.*

abstract class Tool extends ImageProcessor:
  def mousePressed(e: MousePressed): Unit

  def mouseReleased(e: MouseReleased): Unit

  def mouseDragged(e: MouseDragged): Unit

  def mouseWheelMoved(e: ZoomEvent): Unit

  def keyPressed(e: KeyPressed): Unit

  def keyReleased(e: KeyReleased): Unit
