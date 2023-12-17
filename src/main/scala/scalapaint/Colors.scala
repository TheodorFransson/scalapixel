package scalapaint

import java.awt.Color

object Colors:
    private val background = Vector(
        Color(0x121212),
        Color(0x1e1e1e),
        Color(0x232323),
        Color(0x252525),
        Color(0x272727),
        Color(0x2c2c2c),
        Color(0x2e2e2e),
        Color(0x333333),
        Color(0x363636),
        Color(0x383838),
    )

    def backgroundColorDP(dp: Int): Color =
        require(dp > -1 && dp < background.length)
        background(dp)

    def brighten(color: Color, amount: Int): Color =
        new Color(math.min(color.getRed() + amount, 255), math.min(color.getGreen() + amount, 255), math.min(color.getBlue() + amount, 255))
