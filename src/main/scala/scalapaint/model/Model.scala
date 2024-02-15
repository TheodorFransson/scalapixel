package scalapaint.model

import scalapaint.history.{HistoryEntry, HistoryManager}
import scalapaint.image.{EditorImage, ImageProcessor}
import scalapaint.model.Model.Events.*

import java.awt.Graphics2D
import javax.swing.SwingUtilities
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.swing.{Dimension, Publisher, Rectangle}
import scala.swing.event.Event

class Model extends Publisher:
    private var image: EditorImage = EditorImage.ofDim(100, 100)
    private val processQueue: mutable.Queue[() => Future[Unit]] = mutable.Queue()
    private val history: HistoryManager = new HistoryManager()
    private var isProcessing: Boolean = false

    def setNewImage(newImage: EditorImage): EditorImage =
        image = newImage
        publish(NewImage(image))
        history.flushHistory
        image

    def getImage: EditorImage = image

    def enqueueApply(processor: ImageProcessor): Unit =
      processQueue.enqueue(() => applyProcess(processor))
      processNext()

    def enqueueUndo(): Unit =
      if (history.hasUndoHistory) then
        processQueue.enqueue(() => undoProcess(history.popUndo()))
        processNext()

    def enqueueRedo(): Unit=
      if (history.hasRedoHistory) then
        processQueue.enqueue(() => redoProcess(history.popRedo()))
        processNext()

    private def applyProcess(processor: ImageProcessor): Future[Unit] =
        Future {
            val historyEntry = processor.process(getImage)
            val area = historyEntry.getAffectedArea
            history.push(historyEntry)
            SwingUtilities.invokeLater(() => {
              publish(ImageUpdated(image, area))
            })
        }

    private def undoProcess(historyEntry: HistoryEntry): Future[Unit] =
      Future {
        historyEntry.undo(getImage)
        val area = historyEntry.getAffectedArea
        SwingUtilities.invokeLater(() => {
          publish(ImageUpdated(image, area))
        })
      }

    private def redoProcess(historyEntry: HistoryEntry): Future[Unit] =
      Future {
        historyEntry.redo(getImage)
        val area = historyEntry.getAffectedArea
        SwingUtilities.invokeLater(() => {
          publish(ImageUpdated(image, area))
        })
      }

    private def processNext(): Unit =
        if (processQueue.nonEmpty && !isProcessing)
            isProcessing = true
            val operation = processQueue.dequeue()
            operation().onComplete { _ =>
                isProcessing = false
                processNext()
            }

object Model:

  object Events:
    case class ImageUpdated(image: EditorImage, area: Rectangle) extends Event
    case class NewImage(image: EditorImage) extends Event