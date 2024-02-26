package image

import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import scalapixel.image.EditorImage
import shared.TestResources

import java.awt.{Color, Dimension}

class EditorImageTest extends AnyFunSuite with BeforeAndAfter:
	var image: EditorImage = _

	before {
		image = EditorImage.white(new Dimension(40, 30))
	}

	test("EditorImage width and height") {
		assert(image.width == 40)
		assert(image.height == 30)
	}

	test("EditorImage.getColorMatrix") {
		val matrix = image.getColorMatrix

		assert(matrix.forall(arr => arr.forall(color => color == Color.white)))
	}

	test("EditorImage updateInternalBuffer and getInternalBuffer") {
		TestResources.alterImage(image.buffer)
			assert(!TestResources.areBuffersEqual(image.buffer, image.getInternalBuffer()))

		image.updateInteralBuffer()

		assert(TestResources.areBuffersEqual(image.buffer, image.getInternalBuffer()))
	}

	test("EditorImage.createClone") {
		val newImage = image.createClone()

		assert(newImage != image)

		assert(TestResources.areBuffersEqual(newImage.buffer, image.buffer))
	}

	test("EditorImage factory methods") {
		val blueImage = EditorImage.blank(new Dimension(10, 10), Color.blue)
		val whiteImage = EditorImage.white(new Dimension(10, 10))
		val imageOfDim = EditorImage.ofDim(30, 15)

		assert(blueImage.getColorMatrix.forall(arr => arr.forall(color => color == Color.blue)))
		assert(whiteImage.getColorMatrix.forall(arr => arr.forall(color => color == Color.white)))
		assert(imageOfDim.width == 30)
		assert(imageOfDim.height == 15)
	}

