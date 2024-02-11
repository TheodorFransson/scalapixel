package scalapaint.controller

import scalapaint.image.filters.{GaussFilter, ImageFilter}
import scalapaint.model.Model
import scalapaint.view.FilterPanel
import scalapaint.view.FilterPanel.Events.*

import scala.swing.*
import scala.swing.Swing.*
import scala.swing.event.*

class FilterPanelController(model: Model, view: FilterPanel) extends Reactor:
    private var selectedFilter: ImageFilter = new GaussFilter
    
    listenTo(view)
    reactions += {
        case ApplyFilter() => model.enqueueApply(selectedFilter)
        case PreviewModeChanged(mode: Boolean) => println(mode)
        case FilterSelectionChanged(selection: ImageFilter) => selectedFilter = selection
        case FilterParameterChanged(value: String) => selectedFilter.setOption(value)
    }