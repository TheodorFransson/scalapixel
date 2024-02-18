package scalapixel.image

import scalapixel.history.HistoryEntry
import scalapixel.image.EditorImage

import scala.swing.Rectangle

trait ImageProcessor:
  def process(image: EditorImage): HistoryEntry
