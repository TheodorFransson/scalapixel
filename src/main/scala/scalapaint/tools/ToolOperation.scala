package scalapaint.tools

import scalapaint.model.Model
import scalapaint.view.CanvasPanel.Events.*

import java.awt.event.MouseEvent

class ToolOperation(model: Model):
  private var lockedButton: Option[Int] = Option.empty

  def mousePressed(event: MousePressedCanvas): Unit = ()

  def mouseReleased(event: MouseReleasedCanvas): Unit = ()

  def mouseDragged(event: MouseDraggedCanvas): Unit = ()

  def mouseWheelMoved(event: ZoomEvent): Unit = ()

  def keyPressed(event: KeyPressedCanvas): Unit = ()

  def keyReleased(event: KeyReleasedCanvas): Unit = ()

  def isPrimary(event: MouseEvent): Boolean = event.getButton == MouseEvent.BUTTON1

  def isSecondary(event: MouseEvent): Boolean = event.getButton == MouseEvent.BUTTON3

  def lockButton(button: Int): Unit =
    require(lockedButton.isEmpty)
    lockedButton = Option(button)

  def unlockButton(): Unit = lockedButton = Option.empty

  def isButtonLocked: Boolean = lockedButton.isDefined

  def sameButtonAsLocked(button: Int): Boolean = lockedButton.map(_ == button).getOrElse(false)

