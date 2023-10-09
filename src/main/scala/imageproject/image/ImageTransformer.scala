package imageproject.image

import java.awt.image.BufferedImage
import java.awt.{Point, Rectangle}

class ImageTransformer(zoomLevel: Double, focusPoint: Point) extends ImageProcessor:
    def process(image: EditorImage): EditorImage = 
        val subImageRect = getSubImageRect(image.width, image.height)
        EditorImage(image.buffer.getSubimage(subImageRect.x, subImageRect.y, subImageRect.width, subImageRect.height))

    def getVisibleRect(width: Int, height: Int): Rectangle = 
        val visibleWidth = (width / zoomLevel).toInt
        val visibleHeight = (height / zoomLevel).toInt
        val startX = (focusPoint.x - visibleWidth / 2).max(0).min(width - visibleWidth)
        val startY = (focusPoint.y - visibleHeight / 2).max(0).min(height - visibleHeight)
        new Rectangle(startX, startY, visibleWidth, visibleHeight)

    def getSubImageRect(width: Int, height: Int): Rectangle =
        val visibleRect = getVisibleRect(width, height)
        new Rectangle(
            visibleRect.x.max(0), 
            visibleRect.y.max(0), 
            visibleRect.width.min(width - visibleRect.x), 
            visibleRect.height.min(height - visibleRect.y)
        )
    
    

       
    
        