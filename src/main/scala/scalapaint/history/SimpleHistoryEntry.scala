package scalapaint.history

import scalapaint.image.EditorImage

import java.awt.image.BufferedImage
import scala.swing.{Point, Rectangle}

class SimpleHistoryEntry extends HistoryEntry:
  private type Snapshot = (BufferedImage, Point)
  private var snapshot: Snapshot = null

  def undo(editorImage: EditorImage): Unit =
    if (!isCollapsable) then
      val buffer = snapshot._1
      val offset = snapshot._2
      editorImage.graphics.drawImage(buffer, offset.x, offset.y, null)

  def getAffectedArea: Rectangle =
    if (!isCollapsable) then
      val buffer = snapshot._1
      val offset = snapshot._2
      new Rectangle(offset.x, offset.y, buffer.getWidth, buffer.getHeight)
    else
      new Rectangle(0,0,0,0)

  def saveSnapshot(originalImage: EditorImage)(bounds: Rectangle = new Rectangle(0, 0, originalImage.width, originalImage.height)): Unit =
    val savedImage = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_RGB)
    val graphics = savedImage.createGraphics()
    graphics.setClip(0, 0, bounds.width, bounds.height)

    originalImage.writeInteralBuffer()

    graphics.drawImage(originalImage.getInternalBuffer(), -bounds.x, -bounds.y, null)
    snapshot = (savedImage, bounds.getLocation)

    collapsable = false