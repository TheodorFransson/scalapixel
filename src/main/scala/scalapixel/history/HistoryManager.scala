package scalapixel.history

import scalapixel.image.{EditorImage, ImageProcessor}

import scala.collection.mutable

class HistoryManager:
  private val entryLimit = 20

  private val undoHistory: mutable.Stack[HistoryEntry] = mutable.Stack()
  private val redoHistory: mutable.Stack[HistoryEntry] = mutable.Stack()

  def pushHistory(historyEntry: HistoryEntry): Unit =
    if (!historyEntry.isCollapsable) then
      push(historyEntry, undoHistory)
      redoHistory.clear()

  def popUndo(): HistoryEntry =
    if (hasUndoHistory) then
      val historyEntry = undoHistory.pop()
      push(historyEntry, redoHistory)
      historyEntry
    else
      throw new IllegalAccessException("HistoryManager has no undo-history!")

  def popRedo(): HistoryEntry =
    if (hasRedoHistory) then
      val historyEntry = redoHistory.pop()
      push(historyEntry, undoHistory)
      historyEntry
    else
      throw new IllegalAccessException("HistoryManager has no redo-history!")

  def flushHistory: Unit =
    undoHistory.clear()
    redoHistory.clear()

  def hasUndoHistory: Boolean = undoHistory.nonEmpty

  def hasRedoHistory: Boolean = redoHistory.nonEmpty

  private def push(historyEntry: HistoryEntry, stack: mutable.Stack[HistoryEntry]): Unit =
    stack.push(historyEntry)
    limitStack(stack)

  private def limitStack(stack: mutable.Stack[HistoryEntry]): Unit =
    if stack.size > entryLimit then
      stack.removeLast()