package scalapaint.image
import scalapaint.image.EditorImage

trait ImageProcessor:
  def process(image: EditorImage): EditorImage

