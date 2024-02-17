package scalapaint.tools.components

import java.awt.{GridBagConstraints, GridBagLayout}
import javax.swing.JPanel
import scala.swing.*

class ParameterGridPanel(components: Seq[(String, Component)]) extends GridPanel(1, 2):
	peer.setLayout(new GridBagLayout())
	val c = new GridBagConstraints()

	c.fill = GridBagConstraints.HORIZONTAL
	c.insets = new Insets(4, 5, 0, 5)
	c.anchor = GridBagConstraints.NORTH
	c.weightx = 1.0
	c.weighty = 0.0

	components.zipWithIndex.foreach { case ((labelText, component), index) =>
		if (index == components.size - 1) then
			c.weighty = 1.0

		c.insets.top = 6
		c.gridx = 0
		c.gridy = index
		val label = new Label(labelText)
		c.anchor = GridBagConstraints.NORTH
		label.horizontalAlignment = Alignment.Right
		peer.add(label.peer, c)

		c.insets.top = 4
		c.gridx = 1
		c.gridy = index
		c.anchor = GridBagConstraints.NORTH
		peer.add(component.peer, c)
	}