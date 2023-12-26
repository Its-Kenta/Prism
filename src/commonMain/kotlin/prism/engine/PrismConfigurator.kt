package prism.engine

import platform.SDL2.*

data class PrismConfigurator(
    var screenWidth: Int = 800,
    var screenHeight: Int = 600,
    var windowTitle: String = "Prism",
    var windowPosX: UInt = SDL_WINDOWPOS_CENTERED,
    var windowPosY: UInt = SDL_WINDOWPOS_CENTERED,
    var windowFlags: SDL_WindowFlags = 0u,
    var rendererFlags: SDL_RendererFlags = SDL_RENDERER_ACCELERATED or SDL_RENDERER_PRESENTVSYNC,
    var initialisationMode: UInt = SDL_INIT_EVERYTHING,
    var rendererHint: String = SDL_HINT_RENDER_SCALE_QUALITY,
    var hintValue: String = "linear",
    var resizable: Boolean = false,
    var fullScreen: Boolean = false
) {
    companion object {
        var resourceLocator: String = "resources"
    }
}