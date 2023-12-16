package imageproject.controller

import imageproject.model.Model
import imageproject.view.FilterPanel
import imageproject.view.FilterPanel.Events.*

import scala.swing.Swing._
import scala.swing._
import scala.swing.event._
import imageproject.image.filters.ImageFilter

class FilterPanelController(model: Model, view: FilterPanel) extends Reactor:
    listenTo(view)
    reactions += {
        case ApplyFilter() => ???
        case PreviewModeChanged(mode: Boolean) => ???
        case FilterSelectionChanged(selection: ImageFilter) => ???
        case FilterParameterChanged(value: String) => ???
    }