package fr.ribesg.kotvox

import org.lwjgl.input.Keyboard

/**
 * @author Ribesg
 */

public class Config {
    public companion object {
        // Screen configuration
        val SCREEN_WIDTH: Int = 1440
        val SCREEN_HEIGHT: Int = 900
        val SCREEN_RATIO: Float = SCREEN_WIDTH / SCREEN_HEIGHT.toFloat()
        val MAX_FPS: Int = 120
        val HORIZONTAL_FOV: Float = 75.toFloat()
        val VERTICAL_FOV: Float = 115.toFloat()
        val VIEW_DISTANCE: Float = 5000.toFloat()

        // Camera configuration
        val SPEED: Float = .01f
        val MOUSE_SENSITIVITY: Float = .05f

        // Keys configuration
        val KEY_SPRINT: Int = Keyboard.KEY_LSHIFT
        val KEY_FORWARD: Int = Keyboard.KEY_Z
        val KEY_BACK: Int = Keyboard.KEY_S
        val KEY_LEFT: Int = Keyboard.KEY_Q
        val KEY_RIGHT: Int = Keyboard.KEY_D
        val KEY_UP: Int = Keyboard.KEY_SPACE
        val KEY_DOWN: Int = Keyboard.KEY_LCONTROL

        // Map configuration
        val OCTREE_DEPTH: Int = 60
        val OCTREE_RADIUS: Long = Long.MAX_VALUE
        val CHUNK_SIZE: Int = (OCTREE_RADIUS / (2L shl (OCTREE_DEPTH - 2)) + 1).toInt()
    }
}
