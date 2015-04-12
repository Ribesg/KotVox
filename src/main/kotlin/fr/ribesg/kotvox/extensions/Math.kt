package fr.ribesg.kotvox.extensions

/**
 * @author Ribesg
 */

/**
 * Math object used for doing maths with Floats without casts everywhere.
 *
 * Directly relies on what {@link java.lang.Math} relies on, {@link java.lang.StrictMath}.
 */
public object Math {

    public fun toRadians(angdeg: Float): Float = StrictMath.toRadians(angdeg.toDouble()).toFloat()

    public fun cos(a: Float): Float = StrictMath.cos(a.toDouble()).toFloat()
    public fun sin(a: Float): Float = StrictMath.sin(a.toDouble()).toFloat()
    public fun tan(a: Float): Float = StrictMath.tan(a.toDouble()).toFloat()

    public fun <T : Comparable<T>> between(min: T, value: T, max: T): T
            = if (value < min) min else if (value > max) max else value
}
