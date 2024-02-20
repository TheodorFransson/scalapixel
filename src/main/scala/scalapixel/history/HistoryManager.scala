package scalapixel.history

import scalapixel.image.{EditorImage, ImageProcessor}

import scala.collection.mutable

class HistoryManager:
  private val entryLimit = 20

  private val undoHistory: mutable.Stack[HistoryEntry] = mutable.Stack()
  private val redoHistory: mutable.Stack[HistoryEntry] = mutable.Stack()

  def push(historyEntry: HistoryEntry): Unit =
    if (!historyEntry.isCollapsable) then
      pushWithLimit(historyEntry, undoHistory)
      redoHistory.clear()

  def popUndo(): HistoryEntry =
    val historyEntry = undoHistory.pop()
    pushWithLimit(historyEntry, redoHistory)
    historyEntry

  def popRedo(): HistoryEntry =
    val historyEntry = redoHistory.pop()
    pushWithLimit(historyEntry, undoHistory)
    historyEntry

  def flushHistory: Unit =
    undoHistory.clear()
    redoHistory.clear()

  def hasUndoHistory: Boolean = undoHistory.nonEmpty

  def hasRedoHistory: Boolean = redoHistory.nonEmpty

  private def pushWithLimit(historyEntry: HistoryEntry, stack: mutable.Stack[HistoryEntry]): Unit =
    stack.push(historyEntry)
    limitStack(stack)

  private def limitStack(stack: mutable.Stack[HistoryEntry]): Unit =
    if stack.size > entryLimit then
      stack.removeLast()