package scalapixel.history

import scalapixel.image.{EditorImage, ImageProcessor}

import java.awt.image.BufferedImage
import scala.swing.{Point, Rectangle}

class SimpleHistoryEntry extends HistoryEntry:
  private type State = (BufferedImage, Point)
  private var initialState: State = null
  private var finalState: State = null
  private var hasSaved: Boolean = false

  def undo(editorImage: EditorImage): Unit =
    if (!isCollapsable) then
      val buffer = initialState._1
      val offset = initialState._2
      editorImage.graphics.drawImage(buffer, offset.x, offset.y, null)

  def redo(editorImage: EditorImage): Unit =
    if (!isCollapsable) then
      val buffer = finalState._1
      val offset = finalState._2
      editorImage.graphics.drawImage(buffer, offset.x, offset.y, null)

  def getAffectedArea: Rectangle =
    if (!isCollapsable) then
      val buffer = initialState._1
      val offset = initialState._2
      new Rectangle(offset.x, offset.y, buffer.getWidth, buffer.getHeight)
    else
      new Rectangle(0,0,0,0)

  def saveInitialState(originalImage: EditorImage): Unit =
    originalImage.writeInteralBuffer()
    hasSaved = true

  def saveFinalState(originalImage: EditorImage)(bounds: Rectangle = new Rectangle(0, 0, originalImage.width, originalImage.height)): Unit =
    if (!hasSaved) System.err.println(
      "Warning: Saving final state before saving the initial state will invalidate the redo method!"
    )

    initialState = captureState(originalImage.getInternalBuffer(), bounds)
    finalState = captureState(originalImage.buffer, bounds)

    collapsable = false
    
  def hasSavedInitialState() = hasSaved

  private def captureState(image: BufferedImage, bounds: Rectangle): State =
    val state = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_RGB)
    val graphics = state.createGraphics()
    graphics.setClip(0, 0, bounds.width, bounds.height)

    graphics.drawImage(image, -bounds.x, -bounds.y, null)
    (state, bounds.getLocation)