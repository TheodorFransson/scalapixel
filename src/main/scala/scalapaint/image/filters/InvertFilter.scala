package scalapaint.image.filters

import scalapaint.image.EditorImage
import java.awt.Color

class InvertFilter extends ImageFilter("Invert"):
    def process(image: EditorImage): EditorImage =
        val outImage = EditorImage.ofDim(image.width, image.height)
        for i <- 0 until image.height do
            for j <- 0 until image.width do
                val r = 255 - ((image.buffer.getRGB(j, i) >> 16) & 0x0ff)
                val g = 255 - ((image.buffer.getRGB(j, i) >> 8) & 0x0ff)
                val b = 255 - (image.buffer.getRGB(j, i) & 0x0ff)

                outImage.buffer.setRGB(j, i, (255 << 24) | (r << 16) | (g << 8) | (b)) 
        outImage