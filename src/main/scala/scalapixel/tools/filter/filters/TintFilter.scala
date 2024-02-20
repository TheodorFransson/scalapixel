package scalapixel.image.filters

import scalapixel.Colors
import scalapixel.image.EditorImage
import scalapixel.image.filters.ImageFilter

import java.awt.Color

class TintFilter extends ImageFilter("Tint", "Applies a tint based on the primary color."):
    def apply(image: EditorImage): Unit =
        val alphaMask = 255 << 24
        val tintColor = Colors.getPrimaryColor()

        for i <- 0 until image.height do
            for j <- 0 until image.width do
                val currentColor = new Color(image.buffer.getRGB(j, i), false)
                val newRed = (tintColor.getRed + currentColor.getRed) / 2
                val newGreen = (tintColor.getGreen + currentColor.getGreen) / 2
                val newBlue = (tintColor.getBlue + currentColor.getBlue) / 2
                val newColor = (alphaMask) | (newRed << 16) | (newGreen << 8) | newBlue
                image.buffer.setRGB(j, i, newColor)