package scalapaint.history

import scalapaint.image.{EditorImage, ImageProcessor}

import scala.collection.mutable

class HistoryManager:
  private val history: mutable.Stack[HistoryEntry] = mutable.Stack()

  def push(historyEntry: HistoryEntry): Unit = history.push(historyEntry)

  def pop(): HistoryEntry =
      var historyEntry = history.pop()
      while (historyEntry.isCollapsable && history.nonEmpty) do
        historyEntry = history.pop()

      historyEntry

  def hasHistory: Boolean = history.nonEmpty