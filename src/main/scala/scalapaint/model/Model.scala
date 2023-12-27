package scalapaint.model

import scalapaint.History
import scalapaint.image.{EditorImage, ImageProcessor}
import scalapaint.model.Model.Events.*

import java.awt.Graphics2D
import javax.swing.SwingUtilities
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.swing.{Dimension, Publisher}
import scala.swing.event.Event

class Model extends Publisher:
    private var image: EditorImage = EditorImage.ofDim(100, 100)
    private val processQueue: mutable.Queue[() => Future[Unit]] = mutable.Queue()
    private var isProcessing: Boolean = false
    private val history: History = new History()

    def setNewImage(newImage: EditorImage): EditorImage =
        image = newImage
        publish(NewImage(image))
        image

    def getImage: EditorImage = image

    def getImageGraphics: Graphics2D = image.graphics

    private def applyProcess(processor: ImageProcessor): Future[Unit] =
        Future {
            processor.process(getImage)
            publish(ImageUpdated(image))
        }

    private def processNext(): Unit =
        if (processQueue.nonEmpty && !isProcessing)
            isProcessing = true
            val operation = processQueue.dequeue()
            operation().onComplete { _ =>
                isProcessing = false
                processNext()
            }

    def enqueueProcess(processor: ImageProcessor): Unit =
        processQueue.enqueue(() => applyProcess(processor))
        processNext()

        // TODO: Add this to history (History keeps track of the process, the process itself keeps track of how to undo it)

    def undoImageProcess(): EditorImage = ??? // TODO: Undo the latest process from history, make undo return an image

    def redoImageProcess(): EditorImage = ???

object Model:

  object Events:
    case class ImageUpdated(image: EditorImage) extends Event
    case class NewImage(image: EditorImage) extends Event