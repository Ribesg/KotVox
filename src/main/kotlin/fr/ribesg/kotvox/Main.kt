package fr.ribesg.kotvox

import fr.ribesg.kotvox.entity.Axes
import fr.ribesg.kotvox.entity.Cube
import fr.ribesg.kotvox.entity.GraphicalEntity
import fr.ribesg.kotvox.gfx.CameraController
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.GL11.*
import org.lwjgl.util.glu.GLU.gluPerspective
import java.util.LinkedList
import java.util.Random

/**
 * @author Ribesg
 */

/**
 * Global {@link Random} instance.
 */
public val rand: Random = Random()

/**
 * Program entry point.
 */
public fun main(args: Array<String>) {
    Main()
}

/**
 * Main class.
 */
private class Main {
    private val timer: Timer
    private val camera: CameraController
    private val gfxEntities: LinkedList<GraphicalEntity>

    init {
        /* Initialization */
        timer = Timer()
        camera = CameraController(-16f, -16f, -16f, -45f, 45f)
        gfxEntities = LinkedList()

        // Initialize Display
        Display.setDisplayMode(DisplayMode(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT))
        Display.create()

        // Initialize GL View
        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        gluPerspective(Config.HORIZONTAL_FOV, Config.SCREEN_RATIO, 0.001f, Config.VIEW_DISTANCE)
        glMatrixMode(GL_MODELVIEW)
        glEnable(GL_DEPTH_TEST)
        glPointSize(5f)
        glLineWidth(2f)

        // Create axes
        gfxEntities.add(Axes(256, 16))

        // Add things
        gfxEntities.add(Cube(1, 1, 1, 8))

        // Hide Mouse pointer
        Mouse.setGrabbed(true)

        /* Main loop */
        loop()

        /* Destroy */
        Display.destroy()
    }

    private fun loop() {
        while (!Display.isCloseRequested()) {
            // Update mouse status
            if (Mouse.isGrabbed() && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                Mouse.setGrabbed(false)
            } else if (!Mouse.isGrabbed() && Mouse.isButtonDown(0)) {
                Mouse.setGrabbed(true)
            }

            // Update data
            val delta = timer.getDelta()
            camera.update(delta)
            gfxEntities.forEach { e -> e.update(delta) }

            // Render data
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            glPushMatrix()
            gfxEntities.forEach { e -> e.render() }
            glPopMatrix()

            // Update screen
            camera.lookThrough()
            Display.update()
            timer.updateFps()
            Display.sync(Config.MAX_FPS)
        }
    }
}


