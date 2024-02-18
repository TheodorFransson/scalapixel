package scalapixel.views

import org.kordamp.ikonli.*
import org.kordamp.ikonli.swing.FontIcon
import scalapixel.EventBinder
import scalapixel.tools.filter.FilterPanel.Events.PreviewModeChanged
import scalapixel.views.components.CanvasPanelScrollBar

import java.awt.Color
import java.awt.Component.LEFT_ALIGNMENT
import java.awt.event.{AdjustmentEvent, AdjustmentListener}
import javax.swing.JPopupMenu.Separator
import javax.swing.JSeparator
import scala.swing.{Alignment, BorderPanel, BoxPanel, Button, Component, Dimension, FlowPanel, Label, Orientation, Panel, Publisher, ScrollBar, Slider, TextField}
import BorderPanel.Position.*
import scala.swing.Swing.{EmptyBorder, HGlue, HStrut}
import scala.swing.event.{AdjustingEvent, ButtonClicked, EditDone, Event, ValueChanged}

class NavigablePanel(var innerPanel: Panel, maximumZoom: Int, minimumZoom: Int, zoomIncrement: Int, fasterZoomThreshold: Int) extends BorderPanel with EventBinder:
  import NavigablePanel.Events.*

  private var adjustingProgrammatically = false

  private val horizontalScrollBar = new CanvasPanelScrollBar():
    peer.setOrientation(Orientation.Horizontal.id)

  private val verticalScrollBar = new CanvasPanelScrollBar()

  private val zoomSlider = new Slider():
    min = minimumZoom
    max = maximumZoom
    value = 10
    maximumSize = new Dimension(100, preferredSize.height)

  private val zoomInButton = new Button():
    icon = FontIcon.of(fluentui.FluentUiFilledAL.ADD_12, 12, Color.white)
    peer.putClientProperty("JButton.buttonType", "roundRect")

  private val zoomOutButton = new Button():
    icon = FontIcon.of(fluentui.FluentUiFilledAL.LINE_HORIZONTAL_1_20, 12, Color.white)
    peer.putClientProperty("JButton.buttonType", "roundRect")

  private val zoomTextField = new TextField:
    text = s"${zoomSlider.value * 10}%"
    columns = 3
    horizontalAlignment = Alignment.Right
    maximumSize = new Dimension(30, preferredSize.height)

  private val boxPanel = new BoxPanel(Orientation.Horizontal):
    contents += zoomTextField
    contents += HStrut(10)
    contents += zoomOutButton
    contents += zoomSlider
    contents += zoomInButton
    contents += HGlue

    border = EmptyBorder(5, 5, 5, 5)

  private val borderPanel = new BorderPanel():
    layout(horizontalScrollBar) = Center
    layout(boxPanel) = South

  layout(innerPanel) = Center
  layout(borderPanel) = South
  layout(verticalScrollBar) = East

  bindToValueChangeEvent(horizontalScrollBar, (delta: Int) => HorizontalScroll(delta))
  bindToValueChangeEvent(verticalScrollBar, (delta: Int) => VerticalScroll(delta))
  bindToValueChangeEvent(zoomSlider, Zoom.apply, () => {
    updateZoomTextField()
    zoomSlider.value
    }, () => {
      !adjustingProgrammatically
  })

  listenTo(zoomInButton, zoomOutButton, zoomTextField)

  reactions += {
    case ButtonClicked(`zoomInButton`) =>
      adjustZoom(true)
    case ButtonClicked(`zoomOutButton`) =>
      adjustZoom(false)
    case EditDone(_) => handleZoomTextFieldInput()
  }

  def setHorizontalScrollbarParameters(value: Int, size: Int, min: Int, max: Int): Unit =
    setScrollbarParameters(horizontalScrollBar, value, size, min, max)

  def setVerticalScrollbarParameters(value: Int, size: Int, min: Int, max: Int): Unit =
    setScrollbarParameters(verticalScrollBar, value, size, min, max)

  def setZoom(value: Int): Unit =
    adjustingProgrammatically = true
    zoomSlider.value = value
    zoomTextField.text = s"${zoomSlider.value * 10}%"
    adjustingProgrammatically = false

  def navigationEnabled(enabled: Boolean): Unit =
    verticalScrollBar.enabled = enabled
    horizontalScrollBar.enabled = enabled
    zoomSlider.enabled = enabled
    zoomInButton.enabled = enabled
    zoomOutButton.enabled = enabled
    zoomTextField.enabled = enabled

  def setInnerPanel(panel: Panel): Unit =
    innerPanel = panel
    layout(panel) = Center

  private def setScrollbarParameters(scrollBar: CanvasPanelScrollBar, value: Int, size: Int, min: Int, max: Int): Unit =
    adjustingProgrammatically = true
    scrollBar.peer.setValues(value, size, min, max)
    adjustingProgrammatically = false
    scrollBar.lastValue = value


  private def adjustZoom(increase: Boolean): Unit =
    val step = if (zoomSlider.value >= fasterZoomThreshold) 5 else 1
    val newValue = if (increase) (zoomSlider.value + step * zoomIncrement).min(maximumZoom)
    else (zoomSlider.value - step).max(minimumZoom)
    zoomSlider.value = newValue

  private def updateZoomTextField(): Unit = zoomTextField.text = s"${zoomSlider.value * 10}%"

  private def handleZoomTextFieldInput(): Unit =
    val input = zoomTextField.text.filter(_.isDigit)
    val percentage = input.toIntOption.getOrElse(zoomSlider.value * 10)
    val sliderValue = (percentage / 10).max(zoomSlider.min).min(zoomSlider.max)
    zoomSlider.value = sliderValue
    zoomTextField.text = s"${sliderValue * 10}%"

  private def bindToValueChangeEvent[T](component: CanvasPanelScrollBar, event: Int => Event): Unit =
    listenTo(component)
    component.reactions += {
      case ValueChanged(_) if !adjustingProgrammatically =>
        val currentValue = component.peer.getValue()
        val delta = currentValue - component.lastValue
        publish(event(delta))
        component.lastValue = currentValue
    }

object NavigablePanel:
  object Events:
    case class VerticalScroll(value: Int) extends Event
    case class HorizontalScroll(value: Int) extends Event
    case class Zoom(value: Int) extends Event