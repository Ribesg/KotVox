package fr.ribesg.kotvox.gfx

import fr.ribesg.kotvox.Config
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11.glLoadIdentity
import org.lwjgl.opengl.GL11.glRotatef
import org.lwjgl.opengl.GL11.glTranslatef

/**
 * @author Ribesg
 */

val speed = Config.SPEED
val fastSpeed = speed * 5
val mouseSensitivity = Config.MOUSE_SENSITIVITY

public class CameraController(
        var posX: Float,
        var posY: Float,
        var posZ: Float,
        var yaw: Float,
        var pitch: Float
                             ) {
    public fun update(delta: Int) {
        if (Mouse.isGrabbed()) {
            // Update horizontal view angle
            this.yaw += Mouse.getDX() * mouseSensitivity

            // Update vertical view angle
            this.pitch -= Mouse.getDY() * mouseSensitivity
            if (this.pitch > Config.VERTICAL_FOV / 2) {
                this.pitch = Config.VERTICAL_FOV / 2
            } else if (this.pitch < -Config.VERTICAL_FOV / 2) {
                this.pitch = -Config.VERTICAL_FOV / 2
            }
        }

        val distance: Float
        if (Keyboard.isKeyDown(Config.KEY_SPRINT)) {
            distance = fastSpeed * delta
        } else {
            distance = speed * delta
        }

        if (Keyboard.isKeyDown(Config.KEY_FORWARD)) {
            // Move forward
            this.posX -= (distance * Math.sin(Math.toRadians(this.yaw.toDouble()))).toFloat()
            this.posY += (distance * Math.tan(Math.toRadians(this.pitch.toDouble()))).toFloat()
            this.posZ += (distance * Math.cos(Math.toRadians(this.yaw.toDouble()))).toFloat()
        }
        if (Keyboard.isKeyDown(Config.KEY_BACK)) {
            // Move backward
            this.posX += (distance * Math.sin(Math.toRadians(this.yaw.toDouble()))).toFloat()
            this.posY -= (distance * Math.tan(Math.toRadians(this.pitch.toDouble()))).toFloat()
            this.posZ -= (distance * Math.cos(Math.toRadians(this.yaw.toDouble()))).toFloat()
        }
        if (Keyboard.isKeyDown(Config.KEY_LEFT)) {
            // Strafe left
            this.posX -= (distance * Math.sin(Math.toRadians((this.yaw - 90).toDouble()))).toFloat()
            this.posZ += (distance * Math.cos(Math.toRadians((this.yaw - 90).toDouble()))).toFloat()
        }
        if (Keyboard.isKeyDown(Config.KEY_RIGHT)) {
            // Strafe right
            this.posX -= (distance * Math.sin(Math.toRadians((this.yaw + 90).toDouble()))).toFloat()
            this.posZ += (distance * Math.cos(Math.toRadians((this.yaw + 90).toDouble()))).toFloat()
        }

        if (Keyboard.isKeyDown(Config.KEY_UP)) {
            this.posY -= distance
        }
        if (Keyboard.isKeyDown(Config.KEY_DOWN)) {
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
