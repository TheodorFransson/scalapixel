package scalapaint.controller

import scalapaint.Colors
import scalapaint.view.ColorPanel
import scalapaint.view.ColorPanel.Events.*
import scalapaint.view.components.ColorSwatch.Events.SwatchColorClicked

import scala.swing.Reactor

class ColorPanelController(view: ColorPanel) extends Reactor:

  listenTo(view, view.getColorSwatch())
  reactions += {
    case SwatchColorClicked(color) =>
      if (view.isSecondarySelected()) then
        Colors.setSecondaryColor(color)
      else
        Colors.setPrimaryColor(color)

      view.updateColorButtons()

    case SwitchButtonClicked() =>
      Colors.switchColors()
      view.updateColorButtons()

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

      view.updateColorButtons()
  }