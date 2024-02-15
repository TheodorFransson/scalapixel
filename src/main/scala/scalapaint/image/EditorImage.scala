package scalapaint.image

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.io.File
import java.awt.{Color, Dimension, Graphics2D, Rectangle}
import scala.collection.mutable.{ArrayBuffer, HashSet}

class EditorImage(val buffer: BufferedImage):
  val width: Int = buffer.getWidth
  val height: Int = buffer.getHeight

  // A buffer used for history management
  private val internalBuffer: BufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

  lazy val graphics: Graphics2D = buffer.createGraphics()
  lazy val internalGraphics: Graphics2D = internalBuffer.createGraphics()

  writeInteralBuffer()

  def getColorMatrix: Array[Array[Color]] =
    val pixels: Array[Array[Color]] = Array.ofDim(height, width)
    
    for i <- 0 until height; j <- 0 until width do
      pixels(i)(j) = new Color(buffer.getRGB(j, i))
    pixels

  def writeInteralBuffer(): Unit = internalGraphics.drawImage(buffer, 0, 0, null)

  def getInternalBuffer(): BufferedImage = internalBuffer

  def createClone(): EditorImage =
    val newBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val graphics = newBuffer.createGraphics()
    graphics.drawImage(buffer, 0, 0, null)
    new EditorImage(newBuffer)

object EditorImage:
  def blank(size: Dimension, color: Color): EditorImage = 
    val buffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB)
    val graphics = buffer.createGraphics()
    graphics.setPaint(color)
    graphics.fillRect(0, 0, size.width, size.height)
    new EditorImage(buffer)

  def ofDim(width: Int, height: Int): EditorImage = new EditorImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB))

  def white(size: Dimension): EditorImage = blank(size, Color.WHITE)
