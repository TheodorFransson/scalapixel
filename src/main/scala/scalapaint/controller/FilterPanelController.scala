package scalapaint.controller

import scalapaint.image.filters.ImageFilter
import scalapaint.model.Model
import scalapaint.view.FilterPanel
import scalapaint.view.FilterPanel.Events.*

import scala.swing.*
import scala.swing.Swing.*
import scala.swing.event.*

class FilterPanelController(model: Model, view: FilterPanel) extends Reactor:
    listenTo(view)
    reactions += {
        case ApplyFilter() => ???
        case PreviewModeChanged(mode: Boolean) => println(mode)
        case FilterSelectionChanged(selection: ImageFilter) => println(selection)
        case FilterParameterChanged(value: String) => println(value)
    }