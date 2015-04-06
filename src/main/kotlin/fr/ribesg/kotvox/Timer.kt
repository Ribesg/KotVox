package fr.ribesg.kotvox;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

/**
 * @author Ribesg
 */
public class Timer {

    var lastFpsTime: Long = 0
    var lastFrameTime: Long = 0
    var fps: Int = 0

    init {
        this.lastFpsTime = getTime()
        this.lastFrameTime = getTime()
    }

    public fun getDelta(): Int {
        val currentTime = getTime();
        val delta = currentTime - this.lastFrameTime
        this.lastFrameTime = currentTime
        return delta.toInt()
    }

    public fun updateFps() {
        if (getTime() - this.lastFpsTime > 1000) {
            Display.setTitle("FPS: " + this.fps);
            this.fps = 0;
            this.lastFpsTime = getTime();
        }
        this.fps++;
    }

    fun getTime(): Long {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
}
