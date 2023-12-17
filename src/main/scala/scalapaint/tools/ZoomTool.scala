package scalapaint.tools

import scalapaint.EditorWindow

import scala.swing.event.MouseReleased
import java.awt.event.MouseEvent
import java.awt.Point

class ZoomTool extends Tool:
    override def mouseReleased(e: MouseReleased, p: Point): Unit =
        val drawnImage = EditorWindow.getDrawnImage()
        e.peer.getButton() match
            case MouseEvent.BUTTON1 => drawnImage.zoomIn(0.5, p)
            case MouseEvent.BUTTON3 => drawnImage.zoomIn(-0.5, p)
            case _ =>