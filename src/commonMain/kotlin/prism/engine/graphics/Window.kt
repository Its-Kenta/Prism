package prism.engine.graphics

import cnames.structs.SDL_Window
import cnames.structs.SDL_Renderer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString
import prism.engine.PrismConfigurator
import prism.engine.interfaces.Disposable
import platform.SDL2.*

class Window(private val config: PrismConfigurator) : Disposable {
    private var ptr: CPointer<SDL_Window> = requireNotNull(
        SDL_CreateWindow(
            config.windowTitle, config.windowPosX.toInt(), config.windowPosY.toInt(),
            config.screenWidth, config.screenHeight, config.windowFlags
        )
    ) { "ERROR: SDL2 failed to create a window! ${SDL_GetError()?.toKString()}" }

    var fullScreen: Boolean = config.fullScreen
        set(value) {
            val flag: UInt = if (value) SDL_WINDOW_FULLSCREEN_DESKTOP else 0u
            SDL_SetWindowFullscreen(ptr, flag)
            field = value
        }

    val isFullScreen: Boolean
        get() { return SDL_GetWindowFlags(ptr) and SDL_WINDOW_FULLSCREEN_DESKTOP != 0u }

    var title: String
        get() {
            return SDL_GetWindowTitle(ptr)?.toKString() ?: ""
        }
        set(value) { SDL_SetWindowTitle(ptr, value) }

    val renderer: CPointer<SDL_Renderer>
        get() {
            return requireNotNull(SDL_GetRenderer(ptr)) { "ERROR: Failed to retrieve the renderer! ${SDL_GetError()?.toKString()}" }
        }

    fun getWindowPointer(): CPointer<SDL_Window> = ptr

    fun setIcon(iconPath: String) {
        val iconSurface = IMG_Load(iconPath)
            ?: throw RuntimeException("Failed to load icon: ${SDL_GetError()?.toKString()}")

        SDL_SetWindowIcon(ptr, iconSurface)
        SDL_FreeSurface(iconSurface)
    }

    override fun dispose() {
        SDL_DestroyWindow(ptr)
    }
}