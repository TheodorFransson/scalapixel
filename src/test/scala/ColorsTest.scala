import org.scalactic.Requirements
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import scalapixel.Colors

class ColorsTest extends AnyFunSuite with BeforeAndAfter:

	test("Colors.backgroundColorAtDepth") {
		for i <- 0 until Colors.backgroundLevels - 1 do
			assert(Colors.backgroundColorAtDepth(i) !== Colors.backgroundColorAtDepth(i + 1))

		assertThrows[IllegalArgumentException] {
			(Colors.backgroundColorAtDepth(Colors.backgroundLevels))
		}
	}

	test("Colors.brighten") {

	}

	test("Colors.setPrimaryColor") {

	}

	test("Colors.setSecondaryColor") {

	}

	test("Colors.switchColors") {

	}