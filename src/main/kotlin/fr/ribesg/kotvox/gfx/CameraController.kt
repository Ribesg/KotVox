package fr.ribesg.kotvox.gfx

import fr.ribesg.kotvox.Camera
import fr.ribesg.kotvox.Keys
import fr.ribesg.kotvox.Perspective

import fr.ribesg.kotvox.extensions.Math
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11.glLoadIdentity
import org.lwjgl.opengl.GL11.glRotatef
import org.lwjgl.opengl.GL11.glTranslatef

/**
 * @author Ribesg
 */

public class CameraController(
        private var posX: Float,
        private var posY: Float,
        private var posZ: Float,
        private var yaw: Float,
        private var pitch: Float
) {

    public fun update(delta: Int) {
        if (Mouse.isGrabbed()) {
            // Update horizontal view angle
            this.yaw += Mouse.getDX() * Camera.MOUSE_SENSITIVITY

            // Update vertical view angle
            this.pitch -= Mouse.getDY() * Camera.MOUSE_SENSITIVITY
            if (this.pitch > Perspective.FOVY) {
                this.pitch = Perspective.FOVY
            } else if (this.pitch < -Perspective.FOVY) {
                this.pitch = -Perspective.FOVY
            }
        }

        val distance: Float
        if (Keyboard.isKeyDown(Keys.SPRINT)) {
            distance = 5 * Camera.SPEED * delta
        } else {
            distance = Camera.SPEED * delta
        }

        if (Keyboard.isKeyDown(Keys.FORWARD)) {
            // Move forward
            this.posX -= distance * Math.sin(Math.toRadians(this.yaw))
            this.posY += distance * Math.tan(Math.toRadians(this.pitch))
            this.posZ += distance * Math.cos(Math.toRadians(this.yaw))
        }
        if (Keyboard.isKeyDown(Keys.BACK)) {
            // Move backward
            this.posX += distance * Math.sin(Math.toRadians(this.yaw))
            this.posY -= distance * Math.tan(Math.toRadians(this.pitch))
            this.posZ -= distance * Math.cos(Math.toRadians(this.yaw))
        }
        if (Keyboard.isKeyDown(Keys.LEFT)) {
            // Strafe left
            this.posX -= distance * Math.sin(Math.toRadians(this.yaw - 90))
            this.posZ += distance * Math.cos(Math.toRadians(this.yaw - 90))
        }
        if (Keyboard.isKeyDown(Keys.RIGHT)) {
            // Strafe right
            this.posX -= distance * Math.sin(Math.toRadians(this.yaw + 90))
            this.posZ += distance * Math.cos(Math.toRadians(this.yaw + 90))
        }

        if (Keyboard.isKeyDown(Keys.UP)) {
            this.posY -= distance
        }
        if (Keyboard.isKeyDown(Keys.DOWN)) {
            this.posY += distance
        }
    }

    public fun lookThrough() {
        glLoadIdentity()
        glRotatef(this.pitch, 1.0f, 0.0f, 0.0f)
        glRotatef(this.yaw, 0.0f, 1.0f, 0.0f)
        glTranslatef(this.posX, this.posY, this.posZ)
    }
}
