package imageproject

import image.EditorImage

import scala.collection.mutable.Stack

class History:
    private var undoStack = Stack[EditorImage]()
    private val redoStack = Stack[EditorImage]()

    private val stackLimit = 10

    def undo(): Unit =
        if undoStack.nonEmpty then 
            redoStack.push(EditorWindow.getCurrentImage())

            val image = undoStack.pop()
            EditorWindow.setCurrentImageWithoutSaving(image)
            EditorWindow.repaintCanvas()

    def redo(): Unit =
        if redoStack.nonEmpty then
            undoStack.push(EditorWindow.getCurrentImage())

            val image = redoStack.pop()
            EditorWindow.setCurrentImageWithoutSaving(image)
            EditorWindow.repaintCanvas()

    def save(): Unit =
        if undoStack.size < stackLimit then
            val image = EditorWindow.getCurrentImage().deepClone
            redoStack.clear()
            undoStack.push(image)
        else
            undoStack = undoStack.take(stackLimit - 1)

        
