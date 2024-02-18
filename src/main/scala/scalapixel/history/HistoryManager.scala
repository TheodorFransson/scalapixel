package scalapixel.history

import scalapixel.image.{EditorImage, ImageProcessor}

import scala.collection.mutable

class HistoryManager:
  private val entryLimit = 20

  private val undoHistory: mutable.Stack[HistoryEntry] = mutable.Stack()
  private val redoHistory: mutable.Stack[HistoryEntry] = mutable.Stack()

  def push(historyEntry: HistoryEntry): Unit =
    if (!historyEntry.isCollapsable) then
      undoHistory.push(historyEntry)

      if undoHistory.size > entryLimit then
        undoHistory.removeLast()

      redoHistory.clear()

  def popUndo(): HistoryEntry =
      val historyEntry = undoHistory.pop()

      redoHistory.push(historyEntry)
      if redoHistory.size > entryLimit then
        redoHistory.removeLast()

      historyEntry

  def popRedo(): HistoryEntry =
    val historyEntry = redoHistory.pop()
    undoHistory.push(historyEntry)

    if undoHistory.size > entryLimit then
      undoHistory.removeLast()

    historyEntry

  def flushHistory: Unit =
    undoHistory.clear()
    redoHistory.clear()

  def hasUndoHistory: Boolean = undoHistory.nonEmpty

  def hasRedoHistory: Boolean = redoHistory.nonEmpty