package scalapixel.image

import scalapixel.history.{HistoryEntry, HistoryManager}
import scalapixel.image.ImageProcessingManager.Events.*
import scalapixel.image.{EditorImage, ImageProcessor}

import java.awt.Graphics2D
import javax.swing.SwingUtilities
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.swing.event.Event
import scala.swing.{Dimension, Publisher, Rectangle}

class ImageProcessingManager extends Publisher:
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
            history.pushHistory(historyEntry)
            SwingUtilities.invokeLater(() => {
              publish(ImageUpdated(image))
            })
        }

    private def undoProcess(historyEntry: HistoryEntry): Future[Unit] =
      Future {
        historyEntry.undo(getImage)
        SwingUtilities.invokeLater(() => {
          publish(ImageUpdated(image))
        })
      }

    private def redoProcess(historyEntry: HistoryEntry): Future[Unit] =
      Future {
        historyEntry.redo(getImage)
        SwingUtilities.invokeLater(() => {
          publish(ImageUpdated(image))
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

object ImageProcessingManager:

  object Events:
    case class ImageUpdated(image: EditorImage) extends Event
    case class NewImage(image: EditorImage) extends Event