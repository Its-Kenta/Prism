package prism.engine.common.types

/**
 * A data class representing a color in RGBA format.
 *
 * @property r The red component of the color. It is an unsigned byte (UByte) ranging from 0 to 255.
 * @property g The green component of the color. It is an unsigned byte (UByte) ranging from 0 to 255.
 * @property b The blue component of the color. It is an unsigned byte (UByte) ranging from 0 to 255.
 * @property a The alpha (transparency) component of the color. It is an unsigned byte (UByte) ranging from 0 (completely transparent) to 255 (completely opaque). By default, it is set to 255.
 */
data class Color(val r: UByte, val g: UByte, val b: UByte, val a: UByte = 255u) {

    /**
     * Companion object for the Color class. It contains predefined constant colors.
     */
    companion object {
        val WHITE = Color(255u, 255u, 255u)
        val BLACK = Color(0u, 0u, 0u)
        val GRAY = Color(128u, 128u, 128u)
        val RED = Color(255u, 0u, 0u)
        val GREEN = Color(0u, 255u, 0u)
        val BLUE = Color(0u, 0u, 255u)
    }
}