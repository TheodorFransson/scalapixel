package history

import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import scalapixel.history.{HistoryManager, SimpleHistoryEntry}
import shared.TestResources

class HistoryManagerTest extends AnyFunSuite with BeforeAndAfter:
	var historyManager: HistoryManager = _
	val dummyHistoryEntry = new SimpleHistoryEntry()
	dummyHistoryEntry.saveInitialState(TestResources.editorImage)
	dummyHistoryEntry.saveFinalState(TestResources.editorImage)()

	before {
		historyManager = new HistoryManager()
	}

	after {
		historyManager.flushHistory
	}

	test("HistoryManager.pushHistory") {
		historyManager.pushHistory(dummyHistoryEntry)
		assert(historyManager.popUndo() === dummyHistoryEntry)
	}

	test("HistoryManager.popUndo") {
		assertThrows[IllegalAccessException] {
			historyManager.popUndo()
		}

		historyManager.pushHistory(dummyHistoryEntry)
		assert(historyManager.popUndo() === dummyHistoryEntry)
	}

	test("HistoryManager.popRedo") {
		assertThrows[IllegalAccessException] {
			historyManager.popRedo()
		}

		historyManager.pushHistory(dummyHistoryEntry)
		historyManager.popUndo()
		assert(historyManager.popRedo() === dummyHistoryEntry)
	}

	test("HistoryManager.hasUndoHistory") {
		assert(historyManager.hasUndoHistory === false)

		historyManager.pushHistory(dummyHistoryEntry)

		assert(historyManager.hasUndoHistory === true)

		historyManager.popUndo()

		assert(historyManager.hasUndoHistory === false)
	}

	test("HistoryManager.hasRedoHistory") {
		assert(historyManager.hasRedoHistory === false)

		historyManager.pushHistory(dummyHistoryEntry)

		assert(historyManager.hasRedoHistory === false)

		historyManager.popUndo()

		assert(historyManager.hasRedoHistory === true)

		historyManager.popRedo()

		assert(historyManager.hasRedoHistory === false)
	}