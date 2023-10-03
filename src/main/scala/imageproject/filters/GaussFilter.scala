package imageproject.filters

import imageproject.image.EditorImage
import java.awt.Color

class GaussFilter extends ImageFilter("Blur"):
    def apply(image: EditorImage,  args: Double*): EditorImage = 
        val middle = if args.length > 0 then args(0).toInt else 4

        val outImage = EditorImage.ofDim(image.width, image.height)
        val matrix = image.getColorMatrix

        val red = matrix.map(_.map(_.getRed().toShort))
        val green = matrix.map(_.map(_.getGreen().toShort))
        val blue = matrix.map(_.map(_.getBlue().toShort))

        val kernel: Array[Array[Short]] = Array(Array(0, 1, 0), Array(1, middle, 1), Array(0, 1, 0)).map(_.map(_.toShort))
        val weight = kernel.map(_.sum).sum

        for i <- 0 until image.height do
            for j <- 0 until image.width do
                if (i == 0 || i == image.height - 1 || j == 0 || j == image.width - 1) then
                    outImage.buffer.setRGB(j, i, image.buffer.getRGB(j, i))
                else 
                    val r = convolve(red, i, j, kernel, weight)
                    val g = convolve(green, i, j, kernel, weight)
                    val b = convolve(blue, i, j, kernel, weight)
                    outImage.buffer.setRGB(j, i, (255 << 24) | (r << 16) | (g << 8) | (b))
        outImage