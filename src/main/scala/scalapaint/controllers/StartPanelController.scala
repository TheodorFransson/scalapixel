package scalapaint.controllers

import scalapaint.EditorWindow
import scalapaint.image.ImageProcessingManager
import scalapaint.image.ImageProcessingManager.Events.NewImage
import scalapaint.views.{NavigablePanel, StartPanel}

import scala.swing.Reactor
import scala.swing.event.UIElementResized

class StartPanelController(model: ImageProcessingManager, startPanel: StartPanel) extends Reactor:
	listenTo(model, startPanel)

	reactions += {
		case NewImage(_) =>
			EditorWindow.showCanvasPanel()
			dispose()
	}

	def dispose(): Unit = deafTo(model, startPanel)