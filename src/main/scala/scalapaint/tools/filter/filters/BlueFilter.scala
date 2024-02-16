package scalapaint.image.filters

import scalapaint.image.EditorImage
import scalapaint.image.filters.ImageFilter

import java.awt.Color

class BlueFilter extends ImageFilter("Blue"):
    def applyFilter(image: EditorImage): Unit =
        for i <- 0 until image.height do
            for j <- 0 until image.width do
                image.buffer.setRGB(j, i, (255 << 24) | (0 << 16) | (0 << 8) | (image.buffer.getRGB(j, i) & 0x0ff))