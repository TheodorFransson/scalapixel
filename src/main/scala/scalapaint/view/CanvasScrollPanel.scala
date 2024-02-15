package scalapaint.view

import scalapaint.EventBinder
import scalapaint.tools.filter.FilterPanel.Events.PreviewModeChanged

import java.awt.event.{AdjustmentEvent, AdjustmentListener}
import scala.swing.{BorderPanel, Orientation, Publisher, ScrollBar}
import BorderPanel.Position.*
import scala.swing.event.{AdjustingEvent, Event, ValueChanged}

class CanvasScrollPanel(canvasPanel: CanvasPanel) extends BorderPanel with EventBinder:
  import CanvasScrollPanel.Events.*

  private var adjustingProgrammatically = false

  private val horizontalScrollBar = new CanvasPanelScrollBar():
    peer.setOrientation(Orientation.Horizontal.id)

  private val verticalScrollBar = new CanvasPanelScrollBar()

  layout(canvasPanel) = Center
  layout(horizontalScrollBar) = South
  layout(verticalScrollBar) = East

  def setHorizontalScrollbarParameters(value: Int, size: Int, min: Int, max: Int): Unit =
    setScrollbarParameters(horizontalScrollBar, value, size, min, max)

  def setVerticalScrollbarParameters(value: Int, size: Int, min: Int, max: Int): Unit =
    setScrollbarParameters(verticalScrollBar, value, size, min, max)

  private def setScrollbarParameters(scrollBar: CanvasPanelScrollBar, value: Int, size: Int, min: Int, max: Int): Unit =
    adjustingProgrammatically = true
    scrollBar.peer.setValues(value, size, min, max)
    adjustingProgrammatically = false
    scrollBar.lastValue = value

  bindToValueChangeEvent(horizontalScrollBar, (delta: Int) => HorizontalScroll(delta))
  bindToValueChangeEvent(verticalScrollBar, (delta: Int) => VerticalScroll(delta))

  def bindToValueChangeEvent[T](component: CanvasPanelScrollBar, event: Int => Event): Unit =
    listenTo(component)
    component.reactions += {
      case ValueChanged(_) if !adjustingProgrammatically =>
        val currentValue = component.peer.getValue()
        val delta = currentValue - component.lastValue
        publish(event(delta))
        component.lastValue = currentValue
    }

object CanvasScrollPanel:
  object Events:
    case class VerticalScroll(value: Int) extends Event
    case class HorizontalScroll(value: Int) extends Event