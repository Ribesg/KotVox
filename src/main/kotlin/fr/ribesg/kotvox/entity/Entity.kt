package fr.ribesg.kotvox.entity

/**
 * @author Ribesg
 */

trait Entity {

    /**
     * Updates this entity based on time delta.
     *
     * @param delta the time delta
     */
    public fun update(delta: Int) {
        // The default action is to do nothing.
        // Some graphical entities may not need to be updated.
    }

    /**
     * Renders this entity.
     *
     * Called between a call to {@link GL11#glPushMatrix()} and
     * a call to {@link GL11#glPopMatrix()}.
     */
    public fun render()
}
