package scalapaint.tools

import scalapaint.EditorWindow

import scala.collection.mutable.Queue
import scala.swing.event.MousePressed
import java.awt.{Color, Point}
import java.awt.image.BufferedImage

class FloodFillToolDeprecated extends ToolDeprecated:
    private val neighbours = Vector((-1, 0), (1, 0), (0, -1), (0, 1))

    override def mousePressed(e: MousePressed, p: Point): Unit = floodFill(p)

    private val queue = Queue[Point]()

    private def floodFill(startPos: Point): Unit = 
        EditorWindow.history.save()
        val image = EditorWindow.getCurrentImage().buffer

        if isInBounds(startPos, image) then
            val startColor = image.getRGB(startPos.x, startPos.y)
            val targetColor = EditorWindow.selectedColor.getRGB()

            queue += startPos

            while !queue.isEmpty do
                fillNeighbour(queue.dequeue, image, startColor, targetColor)
        
    private def fillNeighbour(point: Point, img: BufferedImage, startColor: Int, targetColor: Int): Unit = 
        if isInBounds(point, img) && img.getRGB(point.x, point.y) != targetColor then
            img.setRGB(point.x, point.y, targetColor)

            neighbours.foreach(n => {
                val newPoint = new Point(point.x + n._1, point.y + n._2)
                if isInBounds(newPoint, img) then
                    if img.getRGB(newPoint.x, newPoint.y) == startColor && img.getRGB(newPoint.x, newPoint.y) != targetColor then queue += newPoint
            })
    
    private def isInBounds(point: Point, img: BufferedImage): Boolean = point.y < img.getHeight() && point.y >= 0 && point.x < img.getWidth() && point.x >= 0
            

        