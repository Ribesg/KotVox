package fr.ribesg.kotvox

import fr.ribesg.kotvox.entity.Axes
import fr.ribesg.kotvox.entity.Cube
import fr.ribesg.kotvox.entity.Entity
import fr.ribesg.kotvox.gfx.CameraController
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.GL11
import org.lwjgl.util.glu.GLU
import java.util.LinkedList

/**
 * KotVox class.
 *
 * @author Ribesg
 */
public class KotVox {

    private val timer: Timer
    private val camera: CameraController
    private val gfxEntities: LinkedList<Entity>

    init {
        this.timer = Timer()
        this.camera = CameraController(-16f, -16f, -16f, 45f, 135f)
        this.gfxEntities = LinkedList()

        this.init()
        this.loop()
        this.destroy()
    }

    private fun init() {
        // Initialize Display
        Display.setDisplayMode(DisplayMode(Screen.WIDTH, Screen.HEIGHT))
        Display.create()

        // Initialize GL View
        GL11.glMatrixMode(GL11.GL_PROJECTION)
        GL11.glLoadIdentity()
        GLU.gluPerspective(Perspective.FOVY, Perspective.RATIO, Perspective.NEAR, Perspective.FAR)
        GL11.glMatrixMode(GL11.GL_MODELVIEW)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glPointSize(5f)
        GL11.glLineWidth(2f)

        // Create axes
        gfxEntities.add(Axes(512, 16))

        // Add things
        gfxEntities.add(Cube(1, 1, 1, 8))

        // Hide Mouse pointer
        Mouse.setGrabbed(true)
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
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
            GL11.glPushMatrix()
            gfxEntities.forEach { e -> e.render() }
            GL11.glPopMatrix()

            // Update screen
            camera.lookThrough()
            Display.update()
            timer.updateFps()
            Display.sync(Screen.MAX_FPS)
        }
    }

    private fun destroy() {
        Display.destroy()
    }
}
