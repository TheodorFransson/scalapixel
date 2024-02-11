package scalapaint.image.filters

import scalapaint.image.EditorImage
import java.awt.Color

class SobelFilter extends ImageFilter("Sobel"):
    def applyFilter(image: EditorImage): Unit =
        val threshold = if option.isDefined then option.get.toInt else 122

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
                    image.buffer.setRGB(j, i, 0xFF000000)
                else
                    image.buffer.setRGB(j, i, 0xFFFFFFFF)