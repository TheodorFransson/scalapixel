package scalapaint.tools.components

import java.awt.event.MouseMotionAdapter
import javax.swing.{DefaultListCellRenderer, JComboBox, JList}
import scala.swing.ComboBox

class TooltipComboBox[T](items: Seq[T], toolTips: Seq[String]) extends ComboBox[T](items):
	peer.setRenderer(new DefaultListCellRenderer {
		override def getListCellRendererComponent(list: JList[_], value: Any, index: Int, isSelected: Boolean, cellHasFocus: Boolean) = {
			if (index > -1 && index < toolTips.length) {
				list.setToolTipText(toolTips(index))
			}
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
		}
	})

	peer.addMouseMotionListener(new MouseMotionAdapter() {
		override def mouseMoved(e: java.awt.event.MouseEvent): Unit = {
			val combo = e.getSource.asInstanceOf[JComboBox[_]]
			val index = combo.getSelectedIndex
			if (index >= 0) {
				combo.setToolTipText(toolTips(index))
			}
		}
	})
