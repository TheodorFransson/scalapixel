package scalapaint.model

import scalapaint.History
import scalapaint.image.{EditorImage, ImageProcessor}

class Model extends Publisher[EditorImage]:
    private var image: EditorImage = EditorImage.ofDim(100, 100)
    private val history: History = new History()

    def setImage(newImage: EditorImage): EditorImage = 
        image = newImage
        publish(image)
        image

    def getImage: EditorImage = image

    def applyProcess(processor: ImageProcessor): EditorImage =
        image = processor.process(image)
        publish(image)
        image
        // TODO: Add this to history (History keeps track of the process, the process itself keeps track of how to undo it)

    def undoImageProcess(): EditorImage = ??? // TODO: Undo the latest process from history, make undo return an image

    def redoImageProcess(): EditorImage = ???


trait Publisher[T]:
  private var subscribers: List[Subscriber[T]] = Nil
  
  def subscribe(subscriber: Subscriber[T]): Unit = 
    subscribers ::= subscriber
  
  def unsubscribe(subscriber: Subscriber[T]): Unit = 
    subscribers = subscribers.filterNot(_ == subscriber)
  
  def publish(event: T): Unit = 
    subscribers.foreach(_.notify(event))

trait Subscriber[T]:
  def notify(event: T): Unit

