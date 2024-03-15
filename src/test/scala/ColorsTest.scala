import org.scalactic.Requirements
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.{AnyFunSuite, AsyncFunSuite}
import scalapixel.Colors
import scalapixel.Colors.Events.{PrimaryColorChanged, SecondaryColorChanged}
import shared.{EventTester, TestReactor}

import java.awt.Color
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.FiniteDuration

class ColorsTest extends EventTester:
	before {
		Colors.setPrimaryColor(Color.black)
		Colors.setSecondaryColor(Color.white)
	}

	test("Colors.backgroundColorAtDepth") {
		for i <- 0 until Colors.backgroundLevels - 1 do
			assert(Colors.backgroundColorAtDepth(i) !== Colors.backgroundColorAtDepth(i + 1))

		assertThrows[IllegalArgumentException] {
			(Colors.backgroundColorAtDepth(Colors.backgroundLevels))
		}
	}

	test("Colors.brighten") {
		val black = Color(0x000000)
		val white = Color(0xffffff)

		assert(Colors.brighten(black, 0x10) === Color(0x101010))
		assert(Colors.brighten(white, 0xff) === white)
	}

	test("Colors.setPrimaryColor") {
		val reactor = setUpReactor(Colors)
		val color = new Color(0x010024)

		assert(Colors.getPrimaryColor() !== color)

		Colors.setPrimaryColor(color)

		assert(reactor.receivedExpectedEvent[PrimaryColorChanged])
		assert(Colors.getPrimaryColor() === color)
	}

	test("Colors.setSecondaryColor") {
		val reactor = setUpReactor(Colors)
		val color = new Color(0x010024)

		assert(Colors.getSecondaryColor() !== color)

		Colors.setSecondaryColor(color)

		assert(reactor.receivedExpectedEvent[SecondaryColorChanged])
		assert(Colors.getSecondaryColor() === color)
	}

	test("Colors.switchColors") {
		val reactor = setUpReactor(Colors)

		assert(Colors.getPrimaryColor() === Color.black)
		assert(Colors.getSecondaryColor() === Color.white)

		Colors.switchColors()

		assert(reactor.receivedExpectedEvent[PrimaryColorChanged])
		assert(reactor.receivedExpectedEvent[SecondaryColorChanged])

		assert(Colors.getPrimaryColor() === Color.white)
		assert(Colors.getSecondaryColor() === Color.black)
	}