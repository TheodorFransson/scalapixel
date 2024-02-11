package scalapaint.history

import scalapaint.image.EditorImage

import java.awt.image.BufferedImage
import scala.swing.{Point, Rectangle}

abstract class HistoryEntry:
  protected var collapsable = true
  
  def isCollapsable: Boolean = collapsable

  def undo(editorImage: EditorImage): Unit

  def getAffectedArea: Rectangle