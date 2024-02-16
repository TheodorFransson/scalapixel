package scalapaint.views

import org.kordamp.ikonli.fluentui
import org.kordamp.ikonli.swing.FontIcon
import scalapaint.views.ColorPanel.Events.{ColorChooserClicked, SwitchButtonClicked}
import scalapaint.views.components.ColorSwatch.Events.SwatchColorClicked
import scalapaint.views.components.{ColorButton, ColorSwatch}
import scalapaint.{Colors, EditorWindow, EventBinder}

import java.awt.Color
import javax.swing.{Box, JColorChooser, JDialog, JPanel}
import scala.swing.event.{ButtonClicked, Event}
import scala.swing.{AbstractButton, Alignment, BoxPanel, Button, Color, ColorChooser, Dimension, FlowPanel, Graphics2D, Label, Orientation, Swing, ToggleButton}

class ColorPanel extends BoxPanel(Orientation.Vertical) with EventBinder:
  private val colorChooser = new ColorChooser:
    peer.setPreviewPanel(new JPanel())

    val panels = peer.getChooserPanels
    panels.foreach(panel => if panel.getDisplayName() != "HSV" then peer.removeChooserPanel(panel))

  private val colorDialog = JColorChooser.createDialog(peer, "Choose color", true, colorChooser.peer, null, null)
  private val primaryColorButton = new ColorButton(new Dimension(50, 50), "Primary color")
  private val secondaryColorButton = new ColorButton(new Dimension(50, 50) , "Secondary color", Colors.getSecondaryColor())

  private val primaryBox = new BoxPanel(Orientation.Vertical):
    contents += primaryColorButton

  private val secondaryBox = new BoxPanel(Orientation.Vertical):
    contents += secondaryColorButton

  private val switch = new Button:
    icon = FontIcon.of(fluentui.FluentUiFilledAL.ARROW_SWAP_20, 15, Color.white)
    tooltip = "Switch primary and secondary color"

  private val colorChooserButton = new Button():

    preferredSize = new Dimension(120, 30)
    text = "Custom"


  private val chooserButtonPanel = new BoxPanel(Orientation.Horizontal):
    contents += Swing.HGlue
    contents += colorChooserButton
    contents += Swing.HGlue

  private val boxPanel = new BoxPanel(Orientation.Horizontal):
    contents += primaryBox
    contents += Swing.HStrut(5)
    contents += switch
    contents += Swing.HStrut(5)
    contents += secondaryBox

  private val colorSwatch = new ColorSwatch(Array(
    Color.black, Color.darkGray, Color.gray, Color.lightGray,
    Color.red, Color.orange, Color.yellow, Color.green,
    Color.cyan, Color.blue, Color.magenta, Color.pink
  ))

  contents += Swing.VStrut(10)
  contents += boxPanel
  contents += Swing.VStrut(10)
  contents += chooserButtonPanel
  contents += Swing.VStrut(10)
  contents += colorSwatch
  contents += Swing.VGlue

  bindToEvent(switch, SwitchButtonClicked())
  bindToEvent(colorChooserButton, ColorChooserClicked(colorChooser, colorDialog))

  listenTo(primaryColorButton, secondaryColorButton)
  reactions += {
    case ButtonClicked(button) =>
      toggleSelection(button)
  }

  def updateColorButtons(): Unit =
    primaryColorButton.setColor(Colors.getPrimaryColor())
    secondaryColorButton.setColor(Colors.getSecondaryColor())

  def isSecondarySelected(): Boolean = secondaryColorButton.selected

  def getColorSwatch(): ColorSwatch = colorSwatch

  private def toggleSelection(btn: AbstractButton): Unit =
    primaryColorButton.selected = false
    secondaryColorButton.selected = false
    btn.selected = true

object ColorPanel:
  object Events:
    case class SwitchButtonClicked() extends Event
    case class ColorChooserClicked(colorChooser: ColorChooser, colorDialog: JDialog) extends Event