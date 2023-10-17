package imageproject.view

import scala.swing.Swing.*
import scala.swing.*
import scala.swing.event.*
import BorderPanel.Position.*
import java.awt.{Color}
import javax.swing.SpinnerNumberModel
import javax.swing.JSpinner

class PencilPanel extends BoxPanel(Orientation.Vertical):
    private val widthFeild = new TextField:
        text = "5"
        
    private val model = new SpinnerNumberModel(5, 1, 100, 1)
    private val spinner = new JSpinner(model)

    private var widthFlowpanel = new FlowPanel:
        peer.setAlignmentX(java.awt.Component.RIGHT_ALIGNMENT)

        contents += new Label:
            text = "Width"

        peer.add(spinner)

    widthFlowpanel.maximumSize = widthFlowpanel.preferredSize
    contents += widthFlowpanel