package scalapaint.views

import scalapaint.Colors

import java.awt.{CardLayout, Graphics2D}
import javax.swing.JPanel
import scala.swing.{Dimension, Panel}

class MainPanel extends Panel:
	background = Colors.backgroundColorAtDepth(0)
	focusable = true