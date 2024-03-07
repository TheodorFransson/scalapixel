package scalapixel.controllers

import scalapixel.controllers.FocusListener.Events.{FocusGained, FocusLost}

import java.awt.event
import java.awt.event.{FocusEvent, FocusListener}
import scala.swing.{Component, Publisher}
import scala.swing.event.Event

trait FocusListener extends Publisher:
  val peer = new event.FocusListener:
    override def focusGained(e: FocusEvent): Unit = publish(FocusGained())
    override def focusLost(e: FocusEvent): Unit = publish(FocusLost())

  def listenToFocusEvents(component: Component): Unit =
    component.peer.addFocusListener(peer)

object FocusListener:
  object Events:
    case class FocusGained() extends Event
    case class FocusLost() extends Event

