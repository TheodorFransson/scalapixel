package scalapaint.image

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.io.File
import java.awt.{Color, Dimension, Graphics2D, Rectangle}
import scala.collection.mutable.{ArrayBuffer, HashSet}

class EditorImage(val buffer: BufferedImage):
  val height: Int = buffer.getHeight
  val width: Int = buffer.getWidth
  val tileSize: Int = 10

  lazy val graphics: Graphics2D = buffer.createGraphics()

  private val tiles: Array[Array[BufferedImage]] = Array.ofDim(width / tileSize, height / tileSize)
  private val hasChanged: Array[Array[Boolean]] = Array.ofDim(width / tileSize, height / tileSize)

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

  def setChanged(bounds: Rectangle, value: Boolean = true): Unit =
    val startX = bounds.x / tileSize
    val endX = (bounds.x + bounds.width + tileSize - 1) / tileSize // Ensures inclusion of partial tiles
    val startY = bounds.y / tileSize
    val endY = (bounds.y + bounds.height + tileSize - 1) / tileSize // Ensures inclusion of partial tiles

    for x <- startX until endX do
      for y <- startY until endY do
        hasChanged(x)(y) = value

  def clamp(n: Int, min: Int, max: Int): Int = Math.min(Math.max(n, min), max)

  def getTiles(x1: Int, y1: Int, x2: Int, y2: Int): Array[Array[BufferedImage]] =

    for i <- tiles.indices do
      for j <- tiles(i).indices do
        if (tiles(i)(j) == null) then tiles(i)(j) = buffer.getSubimage(i * tileSize, j * tileSize, tileSize, tileSize)

    tiles

  def getHasChanged: Array[Array[Boolean]] =
    val wasChanged = Array.ofDim[Boolean](hasChanged.length, hasChanged(0).length)
    for (i <- hasChanged.indices) {
      wasChanged(i) = hasChanged(i).clone()
    }
    setChanged(new Rectangle(0, 0, height, width), false)
    wasChanged

object EditorImage:
  def blank(size: Dimension, color: Color): EditorImage = 
    val buffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB)
    val graphics = buffer.createGraphics()
    graphics.setPaint(color)
    graphics.fillRect(0, 0, size.width, size.height)
    new EditorImage(buffer)

  def ofDim(width: Int, height: Int): EditorImage = new EditorImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB))

  def white(size: Dimension): EditorImage = blank(size, Color.WHITE)
