package scalapixel.image.filters

import scalapixel.image.EditorImage

import java.awt.Color

class GaussFilter extends ImageFilter("Blur", "Blurs the image using Gaussian blur."):
	def apply(image: EditorImage): Unit =
		val middle = if option.isDefined then option.get.toInt else 4

		val clone = image.createClone()
		val kernel: Array[Array[Short]] = Array(Array(0, 1, 0), Array(1, middle, 1), Array(0, 1, 0)).map(_.map(_.toShort))
		val weight = kernel.map(_.sum).sum

		for j <- 0 until image.width do
			image.buffer.setRGB(j, 0, clone.buffer.getRGB(j, 0))
			image.buffer.setRGB(j, image.height - 1, clone.buffer.getRGB(j, image.height - 1))

		for i <- 1 until image.height - 1 do
			image.buffer.setRGB(0, i, clone.buffer.getRGB(0, i))
			image.buffer.setRGB(image.width - 1, i, clone.buffer.getRGB(image.width - 1, i))

		for i <- 1 until image.height - 1 do
			for j <- 1 until image.width - 1 do
				val pixel = fastConvolve(clone, i, j, kernel, weight)
				image.buffer.setRGB(j, i, pixel)
