package prism.engine.extensions

import kotlinx.cinterop.*
import prism.engine.common.types.math.Vector2
import platform.SDL2.*

/**
 * Creates an SDL_Rect representing a rectangle with the specified dimensions and coordinates.
 *
 * @param height the height of the rectangle
 * @param width the width of the rectangle
 * @param xCoordinate the x-coordinate of the top-left corner of the rectangle
 * @param yCoordinate the y-coordinate of the top-left corner of the rectangle
 * @return the SDL_Rect representing the rectangle
 */
fun Arena.rectangle(
    height: Int,
    width: Int,
    xCoordinate: Int,
    yCoordinate: Int
): SDL_Rect {
    return alloc<SDL_Rect>().apply {
        h = height
        w = width
        x = xCoordinate
        y = yCoordinate
    }
}

/**
 * Creates an SDL_Rect representing a rectangle with the given size and position.
 *
 * @param size the size of the rectangle
 * @param position the position of the rectangle
 * @return the created SDL_Rect
 */
fun Arena.rectangle(
    size: Vector2,
    position: Vector2
): SDL_Rect {
    return alloc<SDL_Rect>().apply {
        h = size.x.toInt()
        w = size.y.toInt()
        x = position.x.toInt()
        y = position.y.toInt()
    }
}

/**
 * Creates an SDL_Rect representing a rectangle with the given size and position.
 *
 * @param size the size of the rectangle
 * @param position the position of the rectangle
 * @return the created SDL_Rect
 */
fun NativePlacement.rectangle(
    size: Vector2,
    position: Vector2
): SDL_Rect {
    return alloc<SDL_Rect>().apply {
        h = size.x.toInt()
        w = size.y.toInt()
        x = position.x.toInt()
        y = position.y.toInt()
    }
}

/**
 * Creates an SDL_Rect representing a rectangle with the specified dimensions and coordinates.
 *
 * @param height the height of the rectangle
 * @param width the width of the rectangle
 * @param xCoordinate the x-coordinate of the top-left corner of the rectangle
 * @param yCoordinate the y-coordinate of the top-left corner of the rectangle
 * @return the SDL_Rect representing the rectangle
 */
fun NativePlacement.rectangle(
    height: Int,
    width: Int,
    xCoordinate: Int,
    yCoordinate: Int
): SDL_Rect {
    return alloc<SDL_Rect>().apply {
        h = height
        w = width
        x = xCoordinate
        y = yCoordinate
    }
}