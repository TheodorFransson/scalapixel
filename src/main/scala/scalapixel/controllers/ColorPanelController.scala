package scalapixel.controllers

import scalapixel.Colors
import scalapixel.Colors.Events.*
import scalapixel.views.ColorPanel
import scalapixel.views.ColorPanel.Events.*
import scalapixel.views.components.ColorSwatch.Events.SwatchColorClicked

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