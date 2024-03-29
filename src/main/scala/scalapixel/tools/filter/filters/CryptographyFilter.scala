package scalapixel.image.filters

import scalapixel.image.EditorImage
import java.awt.Color

class CryptographyFilter extends ImageFilter("Encrypt", "<html>Encrypts/decrypts the image using<br> an integer key.</html>"):
    def apply(image: EditorImage): Unit =
        import scala.util.Random

        val seed = if option.isDefined then option.get.toInt else Random().nextInt()
        val random = Random(seed)

        for i <- 0 until image.height do
            for j <- 0 until image.width do
                val r = ((image.buffer.getRGB(j, i) >> 16) & 0x0ff) ^ random.nextInt(255)
                val g = ((image.buffer.getRGB(j, i) >> 8) & 0x0ff) ^ random.nextInt(255)
                val b = (image.buffer.getRGB(j, i) & 0x0ff) ^ random.nextInt(255) 

                image.buffer.setRGB(j, i, (255 << 24) | (r << 16) | (g << 8) | (b & 0x0ff))