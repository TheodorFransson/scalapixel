package scalapaint.image

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.io.File
import java.awt.{Color, Dimension, Graphics2D}

class EditorImage(val buffer: BufferedImage):
  val height: Int = buffer.getHeight
  val width: Int = buffer.getWidth

  lazy val graphics: Graphics2D = buffer.createGraphics()

  def getColorMatrix: Array[Array[Color]] =
    val pixels: Array[Array[Color]] = Array.ofDim(height, width)
    
    for i <- 0 until height; j <- 0 until width do
      pixels(i)(j) = new Color(buffer.getRGB(j, i))
    pixels

  def updateImage(pixels: Array[Array[Color]]): Unit =
    val height = pixels.length
    val width = pixels(0).length

    for i <- 0 until height; j <- 0 until width do
      buffer.setRGB(j, i, pixels(i)(j).getRGB)

  def deepClone: EditorImage =
    val colorModel = buffer.getColorModel
    val premult = colorModel.isAlphaPremultiplied
    val raster = buffer.copyData(null)
    val newBuffer = new BufferedImage(colorModel, raster, premult, null)
    new EditorImage(newBuffer)


object EditorImage:
  def blank(size: Dimension, color: Color): EditorImage = 
    val buffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB)
    val graphics = buffer.createGraphics()
    graphics.setPaint(color)
    graphics.fillRect(0, 0, size.width, size.height)
    new EditorImage(buffer)

  def ofDim(width: Int, height: Int): EditorImage = new EditorImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB))

  def white(size: Dimension): EditorImage = blank(size, Color.WHITE)
