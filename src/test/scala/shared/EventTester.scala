package shared

import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.AsyncFunSuite
import scalapixel.image.ImageProcessingManager

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.FiniteDuration
import concurrent.duration.DurationInt
import scala.swing.Publisher

class EventTester extends AsyncFunSuite with BeforeAndAfter with ScalaFutures:
	given ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

	var testTimeout: FiniteDuration = 5.seconds

	def setUpReactor(publisher: Publisher): TestReactor =
		val reactor = new TestReactor
		reactor.listenTo(publisher)
		reactor