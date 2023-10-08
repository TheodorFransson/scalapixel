package imageproject.image.filters

import imageproject.image.EditorImage
import java.awt.Color

class SobelFilter(arg: Option[Double] = None) extends ImageFilter("Sobel"):
    def process(image: EditorImage): EditorImage = 
        val threshold = if arg.isDefined then arg.get.toInt else 122

        val outImage = EditorImage.ofDim(image.width, image.height)
        val matrix = image.getColorMatrix

        val intensity = computeIntensity(image)

        val xSobel: Array[Array[Short]] = Array(Array(-1, 0, 1), Array(-2, 0, 2), Array(-1, 0, 1)).map(_.map(_.toShort))
        val ySobel: Array[Array[Short]] = Array(Array(-1, -2, -1), Array(0, 0, 0), Array(1, 2, 1)).map(_.map(_.toShort))
        val weight = 1

        for i <- 1 until image.height - 1 do
            for j <- 1 until image.width - 1 do
                val xs = convolve(intensity, i, j, xSobel, weight)
                val ys = convolve(intensity, i, j, ySobel, weight)

                val value = math.abs(xs) + math.abs(ys)
                
                if value > threshold then
                    outImage.buffer.setRGB(j, i, 0xFF000000)
                else
                    outImage.buffer.setRGB(j, i, 0xFFFFFFFF)
        outImage