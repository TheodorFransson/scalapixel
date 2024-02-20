package history

import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import scalapixel.history.SimpleHistoryEntry
import scalapixel.image.EditorImage
import shared.TestResources

import java.awt.Rectangle
import java.awt.image.BufferedImage

class SimpleHistoryEntryTest extends AnyFunSuite with BeforeAndAfter:
	var simpleHistory: SimpleHistoryEntry = _

	def alterImage(buffer: BufferedImage): Unit =
		buffer.setRGB(0, 0, 0x000000)

	before {
		simpleHistory = new SimpleHistoryEntry()
	}

	test("SimpleHistoryEntry.isCollapsable") {
		assert(simpleHistory.isCollapsable === true)

		simpleHistory.saveInitialState(TestResources.editorImage)
		simpleHistory.saveFinalState(TestResources.editorImage)()

		assert(simpleHistory.isCollapsable === false)
	}

	test("SimpleHistoryEntry.saveInitialState") {
		val imageClone = TestResources.editorImageClone
		alterImage(imageClone.buffer)

		assert(TestResources.areBuffersEqual(
			imageClone.buffer,
			imageClone.getInternalBuffer())
			=== false)

		simpleHistory.saveInitialState(imageClone)

		assert(TestResources.areBuffersEqual(
			imageClone.buffer,
			imageClone.getInternalBuffer())
			=== true)
	}

	test("SimpleHistoryEntry.saveFinalState") {
		val imageClone = TestResources.editorImageClone

		assertThrows[IllegalStateException] {
			simpleHistory.saveFinalState(imageClone)()
		}

		simpleHistory.saveInitialState(imageClone)
		alterImage(imageClone.buffer)
		simpleHistory.saveFinalState(imageClone)()

		simpleHistory.undo(imageClone)

		assert(TestResources.areBuffersEqual(
			imageClone.buffer,
			TestResources.bufferedImage)
			=== true)
	}

	test("SimpleHistoryEntry.getAffectedArea") {
		val imageClone = TestResources.editorImageClone

		assert(simpleHistory.getAffectedArea === new Rectangle(0, 0, 0, 0))

		simpleHistory.saveInitialState(imageClone)
		alterImage(imageClone.buffer)
		simpleHistory.saveFinalState(imageClone)()

		assert(simpleHistory.getAffectedArea === new Rectangle(0, 0, 50, 50))

		simpleHistory.saveInitialState(imageClone)
		alterImage(imageClone.buffer)
		simpleHistory.saveFinalState(imageClone)(new Rectangle(0, 0, 1, 1))

		assert(simpleHistory.getAffectedArea === new Rectangle(0, 0, 1, 1))
	}

	test("SimpleHistoryEntry.hasSavedFinalState") {
		assert(simpleHistory.hasSavedInitialState() === false)
		simpleHistory.saveInitialState(TestResources.editorImage)
		simpleHistory.saveFinalState(TestResources.editorImage)()
		assert(simpleHistory.hasSavedInitialState() === true)
	}

	test("SimpleHistoryEntry.undo") {
		val imageClone = TestResources.editorImageClone

		assertThrows[IllegalStateException] {
			simpleHistory.undo(imageClone)
		}

		simpleHistory.saveInitialState(imageClone)
		alterImage(imageClone.buffer)
		simpleHistory.saveFinalState(imageClone)()

		assert(TestResources.areBuffersEqual(imageClone.buffer, TestResources.bufferedImage) === false)

		simpleHistory.undo(imageClone)

		assert(TestResources.areBuffersEqual(imageClone.buffer, TestResources.bufferedImage) === true)
	}

	test("SimpleHistoryEntry.redo") {
		val imageClone = TestResources.editorImageClone

		assertThrows[IllegalStateException] {
			simpleHistory.redo(imageClone)
		}

		simpleHistory.saveInitialState(imageClone)
		alterImage(imageClone.buffer)
		simpleHistory.saveFinalState(imageClone)()

		simpleHistory.undo(imageClone)

		assert(TestResources.areBuffersEqual(imageClone.buffer, TestResources.bufferedImage) === true)

		simpleHistory.redo(imageClone)

		assert(TestResources.areBuffersEqual(imageClone.buffer, TestResources.bufferedImage) === false)
	}


