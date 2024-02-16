package scalapaint

import scalapaint.Colors.Events.{PrimaryColorChanged, SecondaryColorChanged}

import java.awt.Color
import scala.swing.Publisher
import scala.swing.event.Event

object Colors extends Publisher:
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

    private var primaryColor: Color = Color.black
    private var secondaryColor: Color = Color.white

    def getPrimaryColor(): Color = primaryColor

    def getSecondaryColor(): Color = secondaryColor

    def setPrimaryColor(color: Color): Unit =
        primaryColor = color
        publish(PrimaryColorChanged(primaryColor))

    def setSecondaryColor(color: Color): Unit =
        secondaryColor = color
        publish(SecondaryColorChanged(secondaryColor))

    def switchColors(): Unit =
        val temp = primaryColor
        setPrimaryColor(secondaryColor)
        setSecondaryColor(temp)

    def backgroundColorAtDepth(depth: Int): Color =
        require(depth > -1 && depth < background.length)
        background(depth)

    def brighten(color: Color, amount: Int): Color =
        new Color(math.min(color.getRed() + amount, 255), math.min(color.getGreen() + amount, 255), math.min(color.getBlue() + amount, 255))

    object Events:
        case class PrimaryColorChanged(color: Color) extends Event
        case class SecondaryColorChanged(color: Color) extends Event