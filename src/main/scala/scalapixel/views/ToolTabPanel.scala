package scalapixel.views

import scalapixel.EditorWindow
import scalapixel.controllers.ColorPanelController
import scalapixel.tools.ToolPanel

import javax.swing.{JColorChooser, JPanel}
import scala.swing.{BoxPanel, ColorChooser, Component, Dimension, Orientation, Panel, TabbedPane}

class ToolTabPanel extends TabbedPane:
  peer.putClientProperty("JTabbedPane.tabType", "card")

  def replaceToolPanel(newPanel: ToolPanel): Unit =
    if (pages.nonEmpty) then
      pages.update(0, new TabbedPane.Page(newPanel.tooltip, newPanel))
    else
      pages.addOne(new TabbedPane.Page(newPanel.tooltip, newPanel))