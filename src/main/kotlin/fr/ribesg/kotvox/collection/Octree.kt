package fr.ribesg.kotvox.collection

/**
 * @author Ribesg
 */

public class Octree<T>(val maxDepth: Int, val radius: Long) {

    // ##                                #
    // ## TOP                            # BOTTOM
    // ##               ^                #               ^
    // ##               |                #               |
    // ##   NORTH_WEST  |  NORTH_EAST    #   NORTH_WEST  |  NORTH_EAST
    // ##               |                #               |
    // ##               |                #               |
    // ## --------------+------------->  # --------------+------------->
    // ##               |<---radius--->  #               |<---radius--->
    // ##               |                #               |
    // ##   SOUTH_WEST  |  SOUTH_EAST    #   SOUTH_WEST  |  SOUTH_EAST
    // ##               |                #               |
    // ##               |                #               |
    // ##                                #

    public companion object {
        val TOP_NORTH_WEST = 0
        val TOP_NORTH_EAST = 1
        val TOP_SOUTH_WEST = 2
        val TOP_SOUTH_EAST = 3
        val BOTTOM_NORTH_WEST = 4
        val BOTTOM_NORTH_EAST = 5
        val BOTTOM_SOUTH_WEST = 6
        val BOTTOM_SOUTH_EAST = 7
    }

    private val root: InternalNode<T>

    init {
        this.root = InternalNode(this, 0.0, 0.0, 0.0, 0)
    }

    public fun get(x: Double, y: Double, z: Double): Leaf<T>? {
        return this.root.get(x, y, z)
    }

    public fun set(x: Double, y: Double, z: Double, data: T) {
        this.root.set(x, y, z, data)
    }

    public fun unset(x: Double, y: Double, z: Double) {
        this.root.unset(x, y, z)
    }

    private abstract class Node<T>(val tree: Octree<T>, val centerX: Double, val centerY: Double, val centerZ: Double) {
        abstract fun get(x: Double, y: Double, z: Double): Leaf<T>?

        abstract fun set(x: Double, y: Double, z: Double, data: T)

        abstract fun unset(x: Double, y: Double, z: Double)

        open fun hasChildren(): Boolean {
            return false
        }
    }

    private class InternalNode<T>(
            tree: Octree<T>,
            centerX: Double,
            centerY: Double,
            centerZ: Double,
            val depth: Int)
    : Node<T>(tree, centerX, centerY, centerZ) {

        private val nodes: Array<Node<T>?>

        private var childrenAmount: Byte = 0

        init {
            this.nodes = arrayOfNulls<Node<T>>(8)
            this.childrenAmount = 0
        }

        fun createNode(i: Int): Node<T>? {

            // Check that we're not erasing an existing node
            if (this.nodes[i] != null) {
                throw IllegalStateException("Node " + i + " already exists")
            }

            // Compute center of node
            val centerX: Double
            val centerY: Double
            val centerZ: Double
            val diff = this.tree.radius / Math.pow(2.0, (this.depth + 1).toDouble())
            when (i) {
                TOP_NORTH_WEST    -> {
                    centerX = this.centerX - diff
                    centerY = this.centerY + diff
                    centerZ = this.centerZ + diff
                }
                TOP_NORTH_EAST    -> {
                    centerX = this.centerX + diff
                    centerY = this.centerY + diff
                    centerZ = this.centerZ + diff
                }
                TOP_SOUTH_WEST    -> {
                    centerX = this.centerX - diff
                    centerY = this.centerY - diff
                    centerZ = this.centerZ + diff
                }
                TOP_SOUTH_EAST    -> {
                    centerX = this.centerX + diff
                    centerY = this.centerY - diff
                    centerZ = this.centerZ + diff
                }
                BOTTOM_NORTH_WEST -> {
                    centerX = this.centerX - diff
                    centerY = this.centerY + diff
                    centerZ = this.centerZ - diff
                }
                BOTTOM_NORTH_EAST -> {
                    centerX = this.centerX + diff
                    centerY = this.centerY + diff
                    centerZ = this.centerZ - diff
                }
                BOTTOM_SOUTH_WEST -> {
                    centerX = this.centerX - diff
                    centerY = this.centerY - diff
                    centerZ = this.centerZ - diff
                }
                BOTTOM_SOUTH_EAST -> {
                    centerX = this.centerX + diff
                    centerY = this.centerY - diff
                    centerZ = this.centerZ - diff
                }
                else              -> throw IllegalArgumentException("Invalid child identifier: " + i)
            }

            if (this.depth == this.tree.maxDepth - 1) {
                // We're at the deepest Internal Node level, create a leaf
                this.nodes[i] = Leaf(this.tree, centerX, centerY, centerZ)
            } else {
                // We creat another Internal Node level
                this.nodes[i] = InternalNode(this.tree, centerX, centerY, centerZ, this.depth + 1)
            }

            ++this.childrenAmount

            // Return built node
            return this.nodes[i]
        }

        private fun select(x: Double, y: Double, z: Double): Int {
            if (x < this.centerX) {
                if (y < this.centerY) {
                    if (z < this.centerZ) {
                        return BOTTOM_SOUTH_WEST
                    } else {
                        return TOP_SOUTH_WEST
                    }
                } else {
                    if (z < this.centerZ) {
                        return BOTTOM_NORTH_WEST
                    } else {
                        return TOP_NORTH_WEST
                    }
                }
            } else {
                if (y < this.centerY) {
                    if (z < this.centerZ) {
                        return BOTTOM_SOUTH_EAST
                    } else {
                        return TOP_SOUTH_EAST
                    }
                } else {
                    if (z < this.centerZ) {
                        return BOTTOM_NORTH_EAST
                    } else {
                        return TOP_NORTH_EAST
                    }
                }
            }
        }

        override fun get(x: Double, y: Double, z: Double): Leaf<T>? {
            return this.nodes[this.select(x, y, z)]?.get(x, y, z)
        }

        override fun set(x: Double, y: Double, z: Double, data: T) {
            val loc = this.select(x, y, z)
            var node: Node<T>? = this.nodes[loc]
            if (node == null) {
                node = this.createNode(loc)
            }
            node!!.set(x, y, z, data)
        }

        override fun unset(x: Double, y: Double, z: Double) {
            val loc = this.select(x, y, z)
            val node = this.nodes[loc]
            if (node != null) {
                node.unset(x, y, z)
                if (!node.hasChildren()) {
                    this.nodes[loc] = null
                    --this.childrenAmount
                }
            } else {
                throw IllegalArgumentException("Nothing was set at <" + x + ';' + y + ';' + z + '>')
            }
        }

        override fun hasChildren(): Boolean {
            return this.childrenAmount > 0
        }
    }

    private class Leaf<T>(tree: Octree<T>, centerX: Double, centerY: Double, centerZ: Double) : Node<T>(tree, centerX, centerY, centerZ) {

        private var data: T = null

        override fun get(x: Double, y: Double, z: Double): Leaf<T> {
            // TODO Check x,y,z... or not?
            return this
        }

        override fun set(x: Double, y: Double, z: Double, data: T) {
            // TODO Check x,y,z... or not?
            this.data = data
        }

        override fun unset(x: Double, y: Double, z: Double) {
            // TODO Check x,y,z... or not?
            this.data = null
        }
    }
}
