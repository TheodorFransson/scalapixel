package scalapixel.image.filters

import scalapixel.history.{HistoryEntry, SimpleHistoryEntry}
import scalapixel.image.EditorImage
import scalapixel.image.ImageProcessor

import scala.swing.Rectangle

abstract case class ImageFilter(name: String, description: String) extends ImageProcessor:
    protected var option: Option[Double] = None

    def process(img: EditorImage): HistoryEntry =
        val historyEntry = new SimpleHistoryEntry()
        historyEntry.saveSnapshot(img)()

        applyFilter(img)

        historyEntry.saveResult(img)()
        historyEntry

    def applyFilter(img: EditorImage): Unit

    def setOption(option: String): ImageFilter =
        this.option = option.toDoubleOption
        this

    protected def computeIntensity(img: EditorImage): Array[Array[Short]] = 
        val intensity: Array[Array[Short]] = Array.ofDim[Short](img.height, img.width)
        val colorMatrix = img.getColorMatrix
        for 
            h <- 0 until img.height
            w <- 0 until img.width
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

    override def toString(): String = name
