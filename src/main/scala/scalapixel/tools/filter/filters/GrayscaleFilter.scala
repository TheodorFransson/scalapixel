package scalapixel.image.filters

import scalapixel.image.EditorImage
import scalapixel.image.filters.ImageFilter
import java.awt.Color

class GrayscaleFilter extends ImageFilter("Grayscale", "Converts color values into grayscale."):
    def apply(image: EditorImage): Unit =
        for i <- 0 until image.height do
            for j <- 0 until image.width do
                val rgb = image.buffer.getRGB(j, i)
                val intens = ((((rgb>>16) & 0x0ff) + ((rgb>>8) & 0x0ff) + (rgb & 0x0ff)) / 3).toShort
                image.buffer.setRGB(j, i, (255 << 24) | (intens << 16) | (intens << 8) | (intens & 0x0ff))