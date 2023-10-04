package imageproject.filters

import imageproject.image.EditorImage

abstract class ImageFilter(val name: String, val argDescriptions: String*):

    def nbrOfArgs = argDescriptions.length

    def apply(img: EditorImage, args: Double*): EditorImage;

    protected def computeIntensity(img: EditorImage): Array[Array[Short]] = 
        val intensity : Array[Array[Short]] = Array.ofDim(img.height, img.width)
        val matrix = img.getColorMatrix
        for 
            w <- 0 until img.width
            h <- 0 until img.height 
        do
            val c = matrix(h)(w)
            intensity(h)(w) = ((c.getRed()+c.getGreen+c.getBlue())/3).toShort
        intensity

    protected def convolve(p: Array[Array[Short]], i: Int, j: Int, kernel: Array[Array[Short]],
            weight: Int): Short =
        var sum : Double = 0;

        for ii <- -1 to 1 do
            for jj <- -1 to 1 do
                sum += p(i + ii)(j + jj) * kernel(ii + 1)(jj + 1);

        Math.round(sum / weight).toShort;

    override def toString(): String = name
