package scalapaint.views

import scalapaint.EditorWindow
import scalapaint.controllers.ColorPanelController

import javax.swing.{JColorChooser, JPanel}
import scala.swing.{BoxPanel, ColorChooser, Component, Dimension, Orientation, Panel, TabbedPane}

class ParameterPanel extends TabbedPane:
  peer.putClientProperty("JTabbedPane.tabType", "card")

  def replaceToolPanel(newPanel: Panel): Unit =
    if (pages.nonEmpty) then
      pages.update(0, new TabbedPane.Page("Tool parameters", newPanel))
    else
      pages.addOne(new TabbedPane.Page("Tool parameters", newPanel))
