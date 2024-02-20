package shared

import scalapixel.image.EditorImage
import shared.TestResources.getClass

import java.awt.image.BufferedImage
import javax.imageio.ImageIO

object TestResources:
	val imagePath: String = "/test-image.png"

	lazy val bufferedImage: BufferedImage =
		val stream = getClass.getResourceAsStream(imagePath)
		try {
			ImageIO.read(stream)
		} finally {
			stream.close()
		}

	lazy val editorImage: EditorImage = new EditorImage(bufferedImage)

	def editorImageClone: EditorImage = editorImage.createClone()

	def areBuffersEqual(imageA: BufferedImage, imageB: BufferedImage): Boolean =
		var equal = true

		if (imageA.getWidth != imageB.getWidth || imageA.getHeight != imageB.getHeight) equal = false

		for (x <- 0 until imageA.getWidth) do
			for (y <- 0 until imageA.getHeight) do
				if imageA.getRGB(x, y) != imageB.getRGB(x, y) then
					equal = false

		equal

