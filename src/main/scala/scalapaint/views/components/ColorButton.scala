package scalapaint.views.components

import scalapaint.{Colors, EditorWindow}

import java.awt.{Color, FontMetrics}
import scala.swing.{Button, Color, Dimension, Graphics2D, Swing, ToggleButton}

class ColorButton(dimensions: Dimension, initColor: Color = Colors.getPrimaryColor()) extends ToggleButton:
  private var color: Color = initColor

  tooltip = "Color picker"

  border = Swing.LineBorder(Colors.backgroundColorAtDepth(7))
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
      g.setColor(Colors.brighten(background, 50))
      g.fillRect(0, 0, peer.getWidth(), peer.getHeight())

    g.setColor(color)
    val margin = 6

    g.fillRect(margin, margin, peer.getWidth() -  2 * margin, peer.getHeight() -  2 * margin)

    super.paintComponent(g)


