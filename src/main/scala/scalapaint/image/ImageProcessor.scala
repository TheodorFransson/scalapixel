package scalapaint.image

import scalapaint.history.HistoryEntry
import scalapaint.image.EditorImage

import scala.swing.Rectangle

abstract class ImageProcessor:
  def process(image: EditorImage): HistoryEntry
