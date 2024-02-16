package scalapaint.controllers

import scalapaint.Colors
import scalapaint.Colors.Events.*
import scalapaint.views.ColorPanel
import scalapaint.views.ColorPanel.Events.*
import scalapaint.views.components.ColorSwatch.Events.SwatchColorClicked

import scala.swing.Reactor

class ColorPanelController(view: ColorPanel) extends Reactor:

  listenTo(view, view.getColorSwatch(), Colors)
  reactions += {
    case SwatchColorClicked(color) =>
      if (view.isSecondarySelected()) then
        Colors.setSecondaryColor(color)
      else
        Colors.setPrimaryColor(color)

    case SwitchButtonClicked() =>
      Colors.switchColors()
    case ColorChooserClicked(colorChooser, colorDialog) =>
      if (view.isSecondarySelected()) then
        colorChooser.color = Colors.getSecondaryColor()
      else
        colorChooser.color = Colors.getPrimaryColor()

      colorDialog.setVisible(true)

      if (view.isSecondarySelected()) then
        Colors.setSecondaryColor(colorChooser.color)
      else
        Colors.setPrimaryColor(colorChooser.color)
    case PrimaryColorChanged(_) | SecondaryColorChanged(_) =>
      view.updateColorButtons()
  }