package scalapaint.image.filters

import scalapaint.image.EditorImage
import scalapaint.image.filters.ImageFilter

import java.awt.Color

class BlueFilter extends ImageFilter("Blue"):
    def process(image: EditorImage): EditorImage = 
        val outImage = EditorImage.ofDim(image.width, image.height)
        for i <- 0 until image.height do
            for j <- 0 until image.width do
                outImage.buffer.setRGB(j, i, (255 << 24) | (0 << 16) | (0 << 8) | (image.buffer.getRGB(j, i) & 0x0ff)) 
        outImage