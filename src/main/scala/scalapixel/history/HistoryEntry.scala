package scalapixel.history

import scalapixel.image.{EditorImage, ImageProcessor}

import java.awt.image.BufferedImage
import scala.swing.{Point, Rectangle}

abstract class HistoryEntry:
  protected var collapsable = true
  
  def isCollapsable: Boolean = collapsable

  def undo(editorImage: EditorImage): Unit

  def redo(editorImage: EditorImage): Unit

  def getAffectedArea: Rectangle