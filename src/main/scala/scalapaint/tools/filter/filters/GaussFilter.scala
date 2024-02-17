package scalapaint.image.filters

import scalapaint.image.EditorImage
import java.awt.Color

class GaussFilter extends ImageFilter("Blur", "Blurs the image using Gaussian blur."):
    def applyFilter(image: EditorImage): Unit =
        val middle = if option.isDefined then option.get.toInt else 4

        val clone = image.createClone()
        val matrix = image.getColorMatrix

        val red = matrix.map(_.map(_.getRed().toShort))
        val green = matrix.map(_.map(_.getGreen().toShort))
        val blue = matrix.map(_.map(_.getBlue().toShort))

        val kernel: Array[Array[Short]] = Array(Array(0, 1, 0), Array(1, middle, 1), Array(0, 1, 0)).map(_.map(_.toShort))
        val weight = kernel.map(_.sum).sum

        for i <- 0 until image.height do
            for j <- 0 until image.width do
                if (i == 0 || i == image.height - 1 || j == 0 || j == image.width - 1) then
                    image.buffer.setRGB(j, i, clone.buffer.getRGB(j, i))
                else 
                    val r = convolve(red, i, j, kernel, weight)
                    val g = convolve(green, i, j, kernel, weight)
                    val b = convolve(blue, i, j, kernel, weight)
                    image.buffer.setRGB(j, i, (255 << 24) | (r << 16) | (g << 8) | (b))