package image

import org.scalactic.Tolerance.convertNumericToPlusOrMinusWrapper
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import scalapixel.image.RenderImage
import shared.TestResources

import scala.swing.{Dimension, Point}

class RenderImageTest extends AnyFunSuite with BeforeAndAfter:
	val renderImage = new RenderImage(TestResources.editorImage)
	val zoomTarget: Point = new Point(0, 0)
	val tolerance = 1e-4
	given Dimension = new Dimension(400, 400)

	before {
		renderImage.reset()
	}

	test("RenderImage.reset") {
		val position = renderImage.getPosition

		renderImage.pan(10, 10)
		renderImage.zoomAbsolute(2)
		renderImage.reset()

		assert(renderImage.getPosition === position)
		assert(renderImage.getZoomFactor === (1.0 +- tolerance))
	}

	test("RenderImage.zoom in and out") {
		renderImage.zoom(1.1, zoomTarget)

		assert(renderImage.getZoomFactor === (1.1 +- tolerance))

		renderImage.zoom(1.1, zoomTarget)

		assert(renderImage.getZoomFactor === (1.2 +- tolerance))

		renderImage.zoom(0.9, zoomTarget)
		renderImage.zoom(0.9, zoomTarget)
		renderImage.zoom(0.9, zoomTarget)

		assert(renderImage.getZoomFactor === (0.9 +- tolerance))
	}

	test("RenderImage.zoom min/max") {
		for i <- 0 to 40 do
			renderImage.zoom(1.1, zoomTarget)

		assert(renderImage.getZoomFactor === (10.0 +- tolerance))

		for i <- 0 to 50 do
			renderImage.zoom(0.9, zoomTarget)

		assert(renderImage.getZoomFactor === (0.1 +- tolerance))
	}

	test("RenderImage.zoom speedup threshold") {
		for i <- 0 to 19 do
			renderImage.zoom(1.1, zoomTarget)

		assert(renderImage.getZoomFactor === (3.0 +- tolerance))

		renderImage.zoom(1.1, zoomTarget)

		assert(renderImage.getZoomFactor === (3.5 +- tolerance))
	}

	test("RenderImage.zoom pan to target") {
		val position = new Point(renderImage.getPosition.x, renderImage.getPosition.y)
		renderImage.zoom(1.1, zoomTarget)

		assert(position !== renderImage.getPosition)
	}

	test("RenderImage.zoomAbsolute") {
		renderImage.zoomAbsolute(40)

		assert(renderImage.getZoomFactor === (4.0  +- tolerance))

		renderImage.zoomAbsolute(5)

		assert(renderImage.getZoomFactor === (0.5  +- tolerance))
	}

	test("RenderImage.pan") {
		val position = new Point(renderImage.getPosition.x, renderImage.getPosition.y)
		position.translate(10, 10)

		renderImage.pan(10, 10)

		assert(renderImage.getPosition === position)

		renderImage.pan(-10, -10)

		assert(renderImage.getPosition !== position)
	}

	test("RenderImage.pan bounds") {
		val bounds = renderImage.getBounds

		renderImage.pan(Int.MinValue, Int.MinValue)

		assert(renderImage.getPosition === bounds.getLocation)

		renderImage.pan(Int.MaxValue, Int.MaxValue)

		assert(renderImage.getPosition.x === bounds.width)
		assert(renderImage.getPosition.y === bounds.height)
	}


	test("RenderImage.getPointOnImage") {
		val position = new Point(renderImage.getPosition.x, renderImage.getPosition.y)
		assert(renderImage.getPointOnImage(position) === new Point(0, 0))

		position.translate(50, 30)

		assert(renderImage.getPointOnImage(position) === new Point(50, 30))

		renderImage.zoomAbsolute(2)

		assert(renderImage.getPointOnImage(position) !== new Point(50, 30))
	}

	test("RenderImage.render") {
		val clone = TestResources.editorImageClone
		TestResources.alterImage(clone.buffer)

		assert(!TestResources.areBuffersEqual(clone.buffer, renderImage.editorImage.buffer))

		renderImage.pan(-renderImage.getPosition.x, -renderImage.getPosition.y)
		renderImage.render(clone.graphics)

		assert(TestResources.areBuffersEqual(clone.buffer, renderImage.editorImage.buffer))
	}
