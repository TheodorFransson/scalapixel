package scalapixel

import scala.swing.Swing._
import scala.swing._
import scala.swing.event._

trait EventBinder:
  this: Publisher =>

  def bindToEvent(component: Publisher, event: => Event, customAction: () => Unit = () => {}): Unit =
    listenTo(component)
    component.reactions += {
      case ButtonClicked(_) =>
        customAction()
        publish(event)
      case SelectionChanged(_) =>
        customAction()
        publish(event)
    }

  def bindToValueChangeEvent[T](component: Publisher, event: T => Event, getValue: () => T, customAction: () => Unit = () => {}, guard: () => Boolean = () => true): Unit =
    listenTo(component)
    component.reactions += {
      case ValueChanged(_) | ButtonClicked(_) | SelectionChanged(_) =>
        if (guard()) then
          customAction()
          publish(event(getValue()))
    }

  def bindToSpinnerChangeEvent(spinner: javax.swing.JSpinner, event: Int => Event, getValue: () => Number): Unit =
    spinner.addChangeListener(new javax.swing.event.ChangeListener {
      override def stateChanged(e: javax.swing.event.ChangeEvent): Unit = {
        publish(event(getValue().intValue))
      }
    })
