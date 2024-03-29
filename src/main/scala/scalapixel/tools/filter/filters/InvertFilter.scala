package scalapixel.image.filters

import scalapixel.image.EditorImage
import java.awt.Color

class InvertFilter extends ImageFilter("Invert", "Inverts the color value for each pixel."):
    def apply(image: EditorImage): Unit =
        for i <- 0 until image.height do
            for j <- 0 until image.width do
                val r = 255 - ((image.buffer.getRGB(j, i) >> 16) & 0x0ff)
                val g = 255 - ((image.buffer.getRGB(j, i) >> 8) & 0x0ff)
                val b = 255 - (image.buffer.getRGB(j, i) & 0x0ff)

                image.buffer.setRGB(j, i, (255 << 24) | (r << 16) | (g << 8) | (b))