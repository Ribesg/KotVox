package fr.ribesg.kotvox

import org.lwjgl.input.Keyboard

/**
 * @author Ribesg
 */

public object Screen {
    public val WIDTH: Int = 1440
    public val HEIGHT: Int = 900
    public val MAX_FPS: Int = 120
}

public object Perspective {
    public val RATIO: Float = Screen.WIDTH / Screen.HEIGHT.toFloat()
    public val FOVY: Float = 70f
    public val NEAR: Float = .0675f
    public val FAR: Float = 4096f
}

public object Camera {
    public val SPEED: Float = .01f
    public val MOUSE_SENSITIVITY: Float = .05f
    public val VERT_ANGLE: Float = 89f
}

public object Keys {
    public val SPRINT: Int = Keyboard.KEY_LSHIFT
    public val FORWARD: Int = Keyboard.KEY_Z
    public val BACK: Int = Keyboard.KEY_S
    public val LEFT: Int = Keyboard.KEY_Q
    public val RIGHT: Int = Keyboard.KEY_D
    public val UP: Int = Keyboard.KEY_SPACE
    public val DOWN: Int = Keyboard.KEY_LCONTROL
}

/*
public object World {
    public val OCTREE_DEPTH: Int = 60
    public val OCTREE_RADIUS: Long = Long.MAX_VALUE
    public val CHUNK_SIZE: Int = (OCTREE_RADIUS / (2L shl (OCTREE_DEPTH - 2)) + 1).toInt()
}
*/
