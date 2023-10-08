package imageproject.image

import imageproject.*
import gui.CanvasPanel

import scala.swing.Swing.*
import scala.swing.*
import scala.swing.event.*
import java.awt.{Color, Graphics2D, Point, geom}
import java.awt.image.BufferedImage
import java.awt.BasicStroke
import imageproject.image.filters.ImageFilter

class DrawnImage(width: Int, height: Int, canvas: CanvasPanel) extends BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB):
    import DrawnImage.*
    private var filter: Option[ImageFilter] = None
    private var lastPoint = new Point(width / 2, height / 2)
    private var corner = new Point(0, 0)

    /** Calls transform to update the image*/
    def update(): Unit = transform()

    /** Increases the zoom level
     * @param levels the increase
     * @param point to zoom into  
     * */
    def zoomIn(levels: Double, point: Point = lastPoint): Unit =
        lastPoint = point
        zoomLevel = math.max(math.min(zoomLevel + levels, 5), 1)

    /** Translates the given point from the canvas position to the image position
     * @param point point to translate
     * @return the translated point
     * */
    def getActualPoint(point: Point): Point =
        var pos = new Point(point.x - canvas.padding, point.y - canvas.padding)
        pos = new Point((pos.x.toDouble / zoomLevel).toInt, (pos.y.toDouble / zoomLevel).toInt)
        pos.x = pos.x + (corner.x.toDouble / zoomLevel).toInt
        pos.y = pos.y + (corner.y.toDouble / zoomLevel).toInt

        pos

    /** Assigns the filter attribute to specified filter and repaints the canvas
     * @param previewFilter the filter to assign
     * */
    def addFilter(previewFilter: ImageFilter): Unit =
        filter = Some(previewFilter)
        canvas.repaint()

    /** Assigns the filter attribute None to remove any filter */
    def removeFilter(): Unit = 
        filter = None
        canvas.repaint()

    /** @return boolean representing if the filter attribute has a value */
    def hasFilter(): Boolean = filter.nonEmpty

    /** @return the current zoomlevel */
    def getZoomLevel(): Double = zoomLevel

    private def getFilteredImage(): BufferedImage =
        val img = canvas.image
        filter match
            case Some(f) => applyFilter(img.deepClone.buffer, f)
            case None => img.buffer

    private def applyFilter(image: BufferedImage, filter: ImageFilter): BufferedImage =
        var filterImage = new EditorImage(image)
        filter.process(filterImage).buffer

    private def getResizedImage(): BufferedImage =
        val filtered = getFilteredImage()

        val newWidth = (canvas.image.width * zoomLevel).toInt
        val newHeight = (canvas.image.height * zoomLevel).toInt

        val resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB)
        val g = resizedImage.createGraphics()
        g.drawImage(filtered, 0, 0, newWidth, newHeight, null)
        g.dispose()

        resizedImage

    private def drawCheckers(): Unit =
        val g = createGraphics()
        g.setColor(canvas.background)
        g.fillRect(0, 0, width, height)
        
        g.setColor(Colors.backgroundColorDP(1))
        for i <- -10 to width by 30 do
            g.drawLine(i, 0, i, height)
            g.drawLine(0, i, width, i)

        g.setColor(Colors.backgroundColorDP(3))
        g.setStroke(new BasicStroke(2))
        for i <- -10 to width by 90 do
            g.drawLine(i, 0, i, height)
            g.drawLine(0, i, width, i)

    private def transform(point: Point = lastPoint): Unit =
        lastPoint = point
        drawCheckers()

        val resized = getResizedImage()
        val scaledPoint = new Point((point.x.toDouble * zoomLevel).toInt, (point.y.toDouble * zoomLevel).toInt)
        val size = new Dimension(math.min(width, resized.getWidth()), math.min(height, resized.getHeight()))

        val pos = new Point(scaledPoint.x - (width / 2), scaledPoint.y - (height / 2))
        pos.x = math.max(pos.x + math.min((resized.getWidth() - (pos.x + size.width)), 0), 0)
        pos.y = math.max(pos.y + math.min((resized.getHeight() - (pos.y + size.height)), 0), 0)

        var subImage = resized.getSubimage(pos.x, pos.y, size.width, size.height)
        val drawPos = new Point((width / 2) - (subImage.getWidth() / 2), (height / 2) - (subImage.getHeight() / 2))

        corner = new Point(pos.x - drawPos.x, pos.y - drawPos.y) 

        createGraphics().drawImage(subImage, drawPos.x, drawPos.y, size.width, size.height, null)

object DrawnImage:
    var zoomLevel: Double = 1