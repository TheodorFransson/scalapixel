package image

import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.{AnyFunSuite, AsyncFunSuite}
import scalapixel.image.{EditorImage, ImageProcessingManager, SimpleImageProcessor}
import scalapixel.image.ImageProcessingManager.Events.{ImageUpdated, NewImage}
import shared.{TestReactor, TestResources}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.FiniteDuration
import concurrent.duration.DurationInt
import scala.swing.Reactor
import scala.swing.event.Event

class ImageProcessingManagerTest extends AsyncFunSuite with BeforeAndAfter with ScalaFutures:
	given ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
	val testTimeout: FiniteDuration = 5.seconds
	var processingManager: ImageProcessingManager = _

	def setUpReactor(): TestReactor =
		val reactor = new TestReactor
		reactor.listenTo(processingManager)
		reactor

	before {
		processingManager = new ImageProcessingManager
	}

	test("ImageProcessingManager.setNewImage") {
		val reactor = setUpReactor()

		val image = TestResources.editorImage
		assert(!TestResources.areBuffersEqual(processingManager.getImage.buffer, image.buffer))

		processingManager.setNewImage(image)

		assert(reactor.receivedExpectedEvent[NewImage])
		assert(TestResources.areBuffersEqual(processingManager.getImage.buffer, image.buffer))
	}

	test("ImageProcessingManager.enqueueApply") {
		val reactor = setUpReactor()
		val processor = new SimpleImageProcessor:
			override def apply(image: EditorImage): Unit =
				TestResources.alterImage(image.buffer)

		processingManager.enqueueApply(processor)

		val result = reactor.future[ImageUpdated].futureValue(timeout(testTimeout))
		assert(result.isInstanceOf[ImageUpdated])
	}

	test("ImageProcessingManager.enqueueUndo") {
		val reactor = setUpReactor()

		val processor = new SimpleImageProcessor:
			override def apply(image: EditorImage): Unit =
				TestResources.alterImage(image.buffer)

		processingManager.enqueueApply(processor)
		processingManager.enqueueUndo()

		val result = reactor.future[ImageUpdated].futureValue(timeout(testTimeout))
		assert(result.isInstanceOf[ImageUpdated])
	}

	test("ImageProcessingManager.enqueueRedo") {
		val reactor = setUpReactor()

		val processor = new SimpleImageProcessor:
			override def apply(image: EditorImage): Unit =
				TestResources.alterImage(image.buffer)

		processingManager.enqueueApply(processor)
		processingManager.enqueueUndo()
		processingManager.enqueueRedo()

		val result = reactor.future[ImageUpdated].futureValue(timeout(testTimeout))
		assert(result.isInstanceOf[ImageUpdated])
	}