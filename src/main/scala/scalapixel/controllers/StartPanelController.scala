package scalapixel.controllers

import scalapixel.EditorWindow
import scalapixel.image.ImageProcessingManager
import scalapixel.image.ImageProcessingManager.Events.NewImage
import scalapixel.views.{NavigablePanel, StartPanel}

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