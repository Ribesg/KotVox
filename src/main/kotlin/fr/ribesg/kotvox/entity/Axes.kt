package fr.ribesg.kotvox.entity

import fr.ribesg.kotvox.gfx.VBOHandler
import fr.ribesg.kotvox.rand
import org.lwjgl.opengl.GL11.*
import java.util.ArrayList

/**
 * @author Ribesg
 */

public class Axes(length: Int, highlightedPoints: Int) : GraphicalEntity {

    val pointSize: Float
    val lineWidth: Float

    val axesHandle: Int
    val axesColorHandle: Int
    val axesSize: Int

    val axesPointsHandle: Int
    val axesPointsColorHandle: Int
    val axesPointsSize: Int

    init {
        // Set size parameters
        this.pointSize = 6f
        this.lineWidth = 2f

        // Create axes vertices matching provided length and create associated VBO
        val axesVertices = array(
                floatArray(0f, 0f, 0f),
                floatArray(length.toFloat(), 0f, 0f),
                floatArray(0f, 0f, 0f),
                floatArray(0f, length.toFloat(), 0f),
                floatArray(0f, 0f, 0f),
                floatArray(0f, 0f, length.toFloat())
                                )
        this.axesHandle = VBOHandler.createNew3D(axesVertices.toCollection(ArrayList<FloatArray>()))
        this.axesSize = axesVertices.size() * 3

        // Create Color VBO
        val axesColors = array(
                floatArray(1f, 0f, 0f),
                floatArray(1f, 0f, 0f),
                floatArray(0f, 1f, 0f),
                floatArray(0f, 1f, 0f),
                floatArray(0f, 0f, 1f),
                floatArray(0f, 0f, 1f)
                              )
        this.axesColorHandle = VBOHandler.createNew3D(axesColors.toCollection(ArrayList<FloatArray>()))

        // Generate points on the axis
        val axesPoints = ArrayList<FloatArray>(length)
        val axesColorsPoints = ArrayList<FloatArray>(length)
        for (i in 1..length) {
            axesPoints.add(floatArray(i.toFloat(), 0f, 0f))
            axesPoints.add(floatArray(0f, i.toFloat(), 0f))
            axesPoints.add(floatArray(0f, 0f, i.toFloat()))
            if (i % highlightedPoints == 0) {
                // Special color for points every "higlightedPoints" point
                axesColorsPoints.add(floatArray(1f, .25f, .75f))
                axesColorsPoints.add(floatArray(1f, .25f, .75f))
                axesColorsPoints.add(floatArray(1f, .25f, .75f))
            } else {
                axesColorsPoints.add(floatArray(1f, 0f, 0f))
                axesColorsPoints.add(floatArray(0f, 1f, 0f))
                axesColorsPoints.add(floatArray(0f, 0f, 1f))
            }
        }
        this.axesPointsHandle = VBOHandler.createNew3D(axesPoints)
        this.axesPointsColorHandle = VBOHandler.createNew3D(axesColorsPoints)
        this.axesPointsSize = axesPoints.size()
    }

    override fun render() {
        // We keep the old point & line sizes to reset them after
        val oldPointSize = glGetFloat(GL_POINT_SIZE)
        val oldLineWidth = glGetFloat(GL_LINE_WIDTH)

        // Use our own config
        glPointSize(this.pointSize)
        glLineWidth(this.lineWidth)

        // Render
        VBOHandler.render(this.axesHandle, this.axesColorHandle, GL_LINES, this.axesSize)
        VBOHandler.render(this.axesPointsHandle, this.axesPointsColorHandle, GL_POINTS, this.axesPointsSize)

        // Set back old values
        glPointSize(oldPointSize)
        glLineWidth(oldLineWidth)
    }
}
