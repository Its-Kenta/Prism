package prism.engine.common.input

import kotlinx.cinterop.*
import platform.SDL2.*

/**
 * Singleton object representing the keyboard input in a Prism.
 * Provides methods to update and query the state of keyboard keys.
 */
object Keyboard {
    // Array to store the previous state of keys
    private val previousKeys = UByteArray(SDL_NUM_SCANCODES.toInt())

    // CPointer to the current state of keys obtained from SDL_GetKeyboardState
    private val currentKeys: CPointer<UByteVar>? = SDL_GetKeyboardState(null)

    // Constant representing zero UByte for comparison
    private val zeroUByte: UByte = 0u

    /**
     * Updates the state of the keyboard keys.
     * This method should be called once per frame to keep track of key changes.
     */
    fun update() {
        currentKeys?.let { keys ->
            for (i in 0 until SDL_NUM_SCANCODES.toInt()) {
                previousKeys[i] = keys[i]
            }
        }
    }

    /**
     * Checks if a specific key identified by the given [scancode] is currently pressed.
     *
     * @param scancode The SDL scancode of the key to check.
     * @return True if the key is currently pressed, false otherwise.
     */
    fun isKeyPressed(scancode: SDL_Scancode): Boolean {
        return currentKeys?.get(scancode.toInt()) != zeroUByte
    }

    /**
     * Checks if a specific key identified by the given [scancode] was just pressed in the current frame.
     *
     * @param scancode The SDL scancode of the key to check.
     * @return True if the key was just pressed, false otherwise.
     */
    fun isKeyJustPressed(scancode: SDL_Scancode): Boolean {
        val currentIndex = scancode.toInt()
        return currentKeys?.get(currentIndex) != 0u.toUByte() && previousKeys[currentIndex] == zeroUByte
    }
}
