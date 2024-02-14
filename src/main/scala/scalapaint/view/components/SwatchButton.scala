package scalapaint.view.components

import scalapaint.Colors

import java.awt.Color
import scala.swing.{Dimension, Graphics2D, Swing, Button}

class SwatchButton(dimensions: Dimension, initColor: Color = Colors.getPrimaryColor()) extends Button:
  private var color: Color = initColor

  tooltip = "Pick color"

  border = Swing.LineBorder(Colors.backgroundColorAtDepth(6))
  peer.setContentAreaFilled(false)

  peer.setPreferredSize(dimensions)
  peer.setMinimumSize(dimensions)
  peer.setMaximumSize(dimensions)

  def getColor: Color = color

  def setColor(color: Color): Unit =
    this.color = color
    repaint()

  override protected def paintComponent(g: Graphics2D): Unit =
    if peer.getModel().isRollover() || peer.getModel().isSelected then
      g.setColor(Colors.brighten(color, 25))
    else
      g.setColor(color)

    g.fillRect(0, 0, peer.getWidth(), peer.getHeight())

    super.paintComponent(g)


