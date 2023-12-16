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
        case PreviewModeChanged(mode: Boolean) => println(mode)
        case FilterSelectionChanged(selection: ImageFilter) => println(selection)
        case FilterParameterChanged(value: String) => println(value)
    }