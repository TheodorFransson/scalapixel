package imageproject.image

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.io.File
import java.awt.Color
import java.awt.Dimension

class EditorImage(val buffer: BufferedImage):
  val height = buffer.getHeight
  val width = buffer.getWidth

  /** Returns a matrix of Color-objects that represents an image */
  def getColorMatrix: Array[Array[Color]] =
    val pixels: Array[Array[Color]] = Array.ofDim(height, width)

    for i <- 0 until height; j <- 0 until width do
      pixels(i)(j) = new Color(buffer.getRGB(j, i))
    pixels

  /** Updates the image in accordance with the given Color-matrix */
  def updateImage(pixels: Array[Array[Color]]): Unit =
    val height = pixels.length
    val width = pixels(0).length

    for i <- 0 until height; j <- 0 until width do
      buffer.setRGB(j, i, pixels(i)(j).getRGB)

  /** @return a deep clone of this instance */
  def deepClone: EditorImage =
    val colorModel = buffer.getColorModel()
    val premult = colorModel.isAlphaPremultiplied()
    val raster = buffer.copyData(null)
    val newBuffer = new BufferedImage(colorModel, raster, premult, null)
    new EditorImage(newBuffer)

/** Factory for [[imageproject.image.EditorImage]] instances */
object EditorImage:

  /** Creates an EditorImage with one color.
   * @param size dimensions of new image
   * @param color of new image  
   * @return a new EditorImage
   * */
  def blank(size: Dimension, color: Color): EditorImage = 
    val buffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB)
    val graphics = buffer.createGraphics()
    graphics.setPaint(color)
    graphics.fillRect(0, 0, size.width, size.height)
    new EditorImage(buffer)

  /** Creates an EditorImage with specified size.
   * @param width of new image
   * @param height of new image  
   * @return a new EditorImage
   * */
  def ofDim(width: Int, height: Int): EditorImage = new EditorImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB))

  /** Creates a white EditorImage with specified size.
   * @param size dimensions of new image
   * @return a new EditorImage
   * */
  def white(size: Dimension): EditorImage = blank(size, Color.WHITE)
