package imageproject

import image.EditorImage

import scala.collection.mutable.Stack

/** A class using stacks in order to keep track of changes. */
class History:
    private var undoStack = Stack[EditorImage]()
    private val redoStack = Stack[EditorImage]()

    private val stackLimit = 10

    /** Updates the current image to use the latest save. */
    def undo(): Unit =
        if undoStack.nonEmpty then 
            redoStack.push(EditorWindow.getCurrentImage())

            val image = undoStack.pop()
            EditorWindow.setCurrentImageWithoutSaving(image)
            EditorWindow.repaintCanvas()

    /** Updates the current image to use the latest undo. */
    def redo(): Unit =
        if redoStack.nonEmpty then
            undoStack.push(EditorWindow.getCurrentImage())

            val image = redoStack.pop()
            EditorWindow.setCurrentImageWithoutSaving(image)
            EditorWindow.repaintCanvas()

     /** Saves the current image and removes the last image if the stack exceeds the stack limit. */
    def save(): Unit =
        if undoStack.size < stackLimit then
            val image = EditorWindow.getCurrentImage().deepClone
            redoStack.clear()
            undoStack.push(image)
        else
            undoStack = undoStack.take(stackLimit - 1)

        
