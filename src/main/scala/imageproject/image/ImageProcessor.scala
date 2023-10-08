package imageproject.image
import imageproject.image.EditorImage

trait ImageProcessor:
  def process(image: EditorImage): EditorImage

