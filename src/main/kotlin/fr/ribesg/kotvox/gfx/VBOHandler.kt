package fr.ribesg.kotvox.gfx

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import java.nio.FloatBuffer
import java.util.HashMap

/**
 * @author Ribesg
 */

public class VBOHandler {
    public companion object {
        val vbos: HashMap<Int, FloatBuffer> = HashMap()

        public fun createNew3D(values: Collection<FloatArray>): Int {
            val handle = glGenBuffers()
            val buffer = BufferUtils.createFloatBuffer(values.size() * 3)
            values.forEach { a -> buffer.put(a) }
            buffer.flip()

            glBindBuffer(GL_ARRAY_BUFFER, handle)
            glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)
            glBindBuffer(GL_ARRAY_BUFFER, 0)

            vbos.put(handle, buffer)
            return handle
        }

        public fun render(verticesHandle: Int, glType: Int, size: Int) {
            glEnableClientState(GL_VERTEX_ARRAY)

            glBindBuffer(GL_ARRAY_BUFFER, verticesHandle)
            glVertexPointer(3, GL_FLOAT, 0, 0)
            glBindBuffer(GL_ARRAY_BUFFER, 0)

            glDrawArrays(glType, 0, size)

            glDisableClientState(GL_VERTEX_ARRAY)
        }

        public fun render(verticesHandle: Int, colorHandle: Int, glType: Int, size: Int) {
            glEnableClientState(GL_VERTEX_ARRAY)
            glEnableClientState(GL_COLOR_ARRAY)

            glBindBuffer(GL_ARRAY_BUFFER, verticesHandle)
            glVertexPointer(3, GL_FLOAT, 0, 0)
            glBindBuffer(GL_ARRAY_BUFFER, 0)

            glBindBuffer(GL_ARRAY_BUFFER, colorHandle)
            glColorPointer(3, GL_FLOAT, 0, 0)
            glBindBuffer(GL_ARRAY_BUFFER, 0)

            glDrawArrays(glType, 0, size)

            glDisableClientState(GL_VERTEX_ARRAY)
            glDisableClientState(GL_COLOR_ARRAY)
        }
    }
}
