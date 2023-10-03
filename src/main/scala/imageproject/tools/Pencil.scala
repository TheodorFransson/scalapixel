package imageproject.tools

import imageproject.EditorWindow

import scala.util.{Try, Success, Failure}
import scala.swing.Swing._
import scala.swing.event._
import java.awt.event.MouseEvent
import java.awt.BasicStroke
import java.awt.{Color, Point, geom}

class PencilTool extends Tool:
    override def mousePressed(e: MousePressed, p: Point): Unit = 
        e.peer.getButton() match
            case MouseEvent.BUTTON1 =>  
                EditorWindow.history.save()
                path = new geom.GeneralPath
                moveTo(p)
            case MouseEvent.BUTTON3 => 
                val color = Try { Color(EditorWindow.getCurrentImage().buffer.getRGB(p.x, p.y)) }
                color match
                    case Success(value) => 
                        EditorWindow.selectedColor = value
                        EditorWindow.repaintColorButton()
                    case _ =>
            case _ =>

       
    override def mouseDragged(e: MouseDragged, p: Point): Unit = lineTo(p)

    override def mouseReleased(e: MouseReleased, p: Point): Unit = 
        lineTo(p)
        path = new geom.GeneralPath

    private var path = new geom.GeneralPath

    draw()
	
    private def lineTo(p: Point): Unit =
        path.lineTo(p.x.toFloat, p.y.toFloat)
        draw()

    private def moveTo(p: Point): Unit =
        path.moveTo(p.x.toFloat, p.y.toFloat)
        draw()

    private def draw(): Unit =
        val g = EditorWindow.getCurrentImage().buffer.createGraphics()
        g.setColor(EditorWindow.selectedColor)
        g.setStroke(new BasicStroke(EditorWindow.getPencilWidth().toFloat, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))
        g.draw(path)