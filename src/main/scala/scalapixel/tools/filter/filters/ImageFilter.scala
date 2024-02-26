package scalapixel.image.filters

import scalapixel.history.{HistoryEntry, SimpleHistoryEntry}
import scalapixel.image.{EditorImage, ImageProcessor, SimpleImageProcessor}

import scala.swing.Rectangle

abstract case class ImageFilter(name: String, description: String) extends SimpleImageProcessor:
    protected var option: Option[Double] = None

    def setOption(option: String): ImageFilter =
        this.option = option.toDoubleOption
        this

    protected def computeIntensity(image: EditorImage): Array[Array[Short]] =
        val intensity: Array[Array[Short]] = Array.ofDim[Short](image.height, image.width)
        val colorMatrix = image.getColorMatrix
        for
            h <- 0 until image.height
            w <- 0 until image.width
        do
            val pixelColor = colorMatrix(h)(w)
            intensity(h)(w) = ((pixelColor.getRed + pixelColor.getGreen + pixelColor.getBlue) / 3).toShort
        intensity

    protected def convolve(imageMatrix: Array[Array[Short]], i: Int, j: Int, kernel: Array[Array[Short]], weight: Int): Short = 
        var sum: Double = 0.0
        for 
            ii <- -1 to 1
            jj <- -1 to 1
        do
            sum += imageMatrix(i + ii)(j + jj) * kernel(ii + 1)(jj + 1)
        (sum / weight).round.toShort

    protected def fastConvolve(image: EditorImage, i: Int, j: Int, kernel: Array[Array[Short]], weight: Int): Int =
        var sumR: Double = 0.0
        var sumG: Double = 0.0
        var sumB: Double = 0.0

        for
            ii <- -1 to 1
            jj <- -1 to 1
        do
            val pixel = image.buffer.getRGB(j + jj, i + ii)
            val red = (pixel >> 16) & 0xFF
            val green = (pixel >> 8) & 0xFF
            val blue = pixel & 0xFF

            sumR += red * kernel(ii + 1)(jj + 1)
            sumG += green * kernel(ii + 1)(jj + 1)
            sumB += blue * kernel(ii + 1)(jj + 1)

        val r = ((sumR / weight).round.toInt & 0xFF) << 16
        val g = ((sumG / weight).round.toInt & 0xFF) << 8
        val b = (sumB / weight).round.toInt & 0xFF

        (0xFF << 24) | r | g | b

    override def toString(): String = name
