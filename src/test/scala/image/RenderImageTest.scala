package image

import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import scalapixel.image.RenderImage
import shared.TestResources

import scala.swing.Dimension

class RenderImageTest extends AnyFunSuite with BeforeAndAfter:
	val renderImage = new RenderImage(TestResources.editorImage)
	given Dimension = new Dimension(400, 400)

	before {
		renderImage.reset()
	}

	test("RenderImage.reset") {
		val position = renderImage.getPosition

		renderImage.pan(10, 10)
		renderImage.zoomAbsolute(2)
		renderImage.reset()

		assert(renderImage.getPosition == position)
		assert(renderImage.getZoomFactor == 1.0)
	}

	test("RenderImage.zoom") {

	}

	test("RenderImage.zoomAbsolute") {

	}

	test("RenderImage.pan") {

	}

	test("RenderImage.getBounds") {

	}

	test("RenderImage.getPosition") {

	}

	test("RenderImage.getZoomFactor") {

	}

	test("RenderImage.getPointOnImage") {

	}

	test("RenderImage.render") {

	}
