package scalapixel.image
import scalapixel.history.{HistoryEntry, SimpleHistoryEntry}

trait SimpleImageProcessor extends ImageProcessor:
	override def process(image: EditorImage): HistoryEntry =
		val historyEntry = new SimpleHistoryEntry()
		historyEntry.saveInitialState(image)

		apply(image)

		historyEntry.saveFinalState(image)()
		historyEntry

	def apply(image: EditorImage): Unit
