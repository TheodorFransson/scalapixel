package scalapaint.image
import scalapaint.image.EditorImage

import scala.swing.Rectangle

trait ImageProcessor:
  def process(image: EditorImage): Unit
  def undo(image: EditorImage): Unit
  def getAffectedArea(): Rectangle

