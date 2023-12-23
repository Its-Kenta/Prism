package prism.engine.graphics

import cnames.structs.SDL_Texture
import kotlinx.cinterop.*
import prism.engine.Prism
import prism.engine.common.types.math.Vector2
import platform.SDL2.*

/**
 * Type alias for an SDL_Texture pointer.
 */
typealias Texture = CPointer<SDL_Texture>

fun loadTexture(filePath: String): Texture {
        if (Platform.isDebugBinary) SDL_LogMessage(SDL_LogCategory.SDL_LOG_CATEGORY_APPLICATION.ordinal, SDL_LOG_PRIORITY_INFO, "Loading %s", filePath)
        val texture: CPointer<SDL_Texture> = requireNotNull(IMG_LoadTexture(Prism.window.renderer, filePath)) { "ERROR: Failed to load texture! ${SDL_GetError()?.toKString()}" }
        return texture
}

fun Texture.getDimensions(): Vector2 = memScoped {
    val w = alloc<IntVar>()
    val h = alloc<IntVar>()

    SDL_QueryTexture(this@getDimensions, null, null, w.ptr, h.ptr)
    Vector2(w.value.toFloat(), h.value.toFloat())
}

val Texture.width: Int
    get() {
        return memScoped {
            alloc<IntVar>().usePinned { intVar ->
                SDL_QueryTexture(this@width, null, null, intVar.get().ptr, null)
                intVar.get().value
            }
        }
    }

val Texture.height: Int
    get() {
        return memScoped {
            alloc<IntVar>().usePinned { intVar ->
                SDL_QueryTexture(this@height, null, null, null, intVar.get().ptr)
                intVar.get().value
            }
        }
    }