package scalapaint.tools

import scala.swing.event.MousePressed
import scala.swing.event.MouseReleased
import scala.swing.event.MouseDragged
import scala.swing.event.MouseWheelMoved

import java.awt.Point

abstract class ToolDeprecated:

    def mousePressed(e: MousePressed, p: Point): Unit = activate(e.point)

    def mouseReleased(e: MouseReleased, p: Point): Unit = activate(e.point)

    def mouseDragged(e: MouseDragged, p: Point): Unit = activate(e.point)

    def mouseWheelMoved(e: MouseWheelMoved, p: Point): Unit = activate(e.point)

    def activate(point: Point): Unit = None
