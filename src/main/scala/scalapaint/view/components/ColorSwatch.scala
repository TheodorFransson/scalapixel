package scalapaint.view.components

import scalapaint.EventBinder
import scalapaint.view.components.ColorSwatch.Events.SwatchColorClicked

import java.awt.{Component, GridBagConstraints, GridBagLayout}
import javax.swing.JPanel
import scala.swing.event.{ButtonClicked, Event}
import scala.swing.{Alignment, Color, Dimension, FlowPanel, GridPanel, Insets, Panel, Swing}

class ColorSwatch(colors: Array[Color]) extends Panel with EventBinder:
  preferredSize = new Dimension(4 * 30 + 14, 3 * 30 + 10)
  peer.setLayout(new GridBagLayout)
  val c = new GridBagConstraints()

  c.fill = GridBagConstraints.NONE
  c.weightx = 0
  c.weighty = 0
  c.anchor = GridBagConstraints.NORTH
  c.insets = new Insets(2, 2, 2, 2)

  for (i <- colors.indices) {
    val button = new SwatchButton(new Dimension(30, 30), colors(i))

    bindToEvent(button, SwatchColorClicked(button.getColor))

    c.gridx = i % 4
    c.gridy = i / 4

    peer.add(button.peer, c)
  }

  c.weighty = 0.1
  peer.add(new JPanel, c)

object ColorSwatch:
  object Events:
    case class SwatchColorClicked(color: Color) extends Event

