package imageproject.filters

import imageproject.image.EditorImage
import java.awt.Color

class GrayscaleFilter extends ImageFilter("Grayscale"):
    def apply(image: EditorImage,  args: Double*): EditorImage = 
        val outImage = EditorImage.ofDim(image.width, image.height)
         for i <- 0 until image.height do
            for j <- 0 until image.width do
                val rgb = image.buffer.getRGB(j, i)
                val intens = ((((rgb>>16) & 0x0ff) + ((rgb>>8) & 0x0ff) + (rgb & 0x0ff)) / 3).toShort
                outImage.buffer.setRGB(j, i, (255 << 24) | (intens << 16) | (intens << 8) | (intens & 0x0ff))
        outImage