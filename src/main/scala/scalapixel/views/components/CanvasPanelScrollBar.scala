package scalapixel.views.components

import scala.swing.ScrollBar

class CanvasPanelScrollBar extends ScrollBar:
  me =>
  peer.addAdjustmentListener(new java.awt.event.AdjustmentListener {
    def adjustmentValueChanged(e: java.awt.event.AdjustmentEvent) = {
      publish(new swing.event.ValueChanged(me))
    }
  })

  var lastValue = peer.getValue
