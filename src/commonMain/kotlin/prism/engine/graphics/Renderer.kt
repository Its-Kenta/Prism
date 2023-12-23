package prism.engine.graphics

import cnames.structs.SDL_Renderer
import kotlinx.cinterop.*
import prism.engine.PrismConfigurator
import prism.engine.common.types.Color
import prism.engine.common.types.math.Vector2
import prism.engine.interfaces.Disposable
import platform.SDL2.*

class Renderer(window: Window, private val config: PrismConfigurator) : Disposable {
    private var ptr: CPointer<SDL_Renderer> = requireNotNull(SDL_CreateRenderer(window.getWindowPointer(), -1, config.rendererFlags)) {
        "ERROR: SDL2 failed to create a renderer! ${SDL_GetError()?.toKString()}"
    }
    fun present() {
        SDL_RenderPresent(ptr)
    }
    fun clearBackground(color: Color) {
        SDL_SetRenderDrawColor(ptr, color.r, color.g, color.b, color.a)
        SDL_RenderClear(ptr)
    }
    fun drawFilledRectangle(rectangle: SDL_Rect, color: Color) {
        SDL_SetRenderDrawColor(ptr, color.r, color.g, color.b, color.a)
        SDL_RenderFillRect(ptr, rectangle.ptr)
    }
    fun drawRectangle(rectangle: SDL_Rect, color: Color = Color.WHITE) {
        SDL_SetRenderDrawColor(ptr, color.r, color.g, color.b, color.a)
        SDL_RenderDrawRect(ptr, rectangle.ptr)
    }
    fun drawTexture(texture: Texture, position: Vector2, scale: Vector2 = Vector2(0F, 0F)) {
        memScoped {
            val dest = alloc<SDL_Rect>().apply {
                this.x = position.x.toInt()
                this.y = position.y.toInt()
            }

            val w = alloc<IntVar>()
            val h = alloc<IntVar>()

            SDL_QueryTexture(texture, null, null, w.ptr, h.ptr)

            dest.w = w.value
            dest.h = h.value

            SDL_RenderCopy(ptr, texture, null, dest.ptr)
        }
    }
    fun setViewport(rectangle: SDL_Rect) {
        SDL_RenderSetViewport(ptr, rectangle.ptr)
    }
    fun drawLine(start: Vector2, end: Vector2, color: Color) {
        SDL_SetRenderDrawColor(ptr, color.r, color.g, color.b, color.a)
        SDL_RenderDrawLine(ptr, start.x.toInt(), start.y.toInt(), end.x.toInt(), end.y.toInt())
    }
    fun drawPoint(position: Vector2, color: Color) {
        SDL_SetRenderDrawColor(ptr, color.r, color.g, color.b, color.a)
        SDL_RenderDrawPoint(ptr, position.x.toInt(), position.y.toInt())
    }
    fun drawGrid(cellSize: Int, color: Color) {
        SDL_SetRenderDrawColor(ptr, color.r, color.g, color.b, color.a)

        for (i in 0 until config.screenWidth step cellSize) {
            SDL_RenderDrawLine(ptr, i, 0, i, config.screenHeight)
        }

        for (j in 0 until config.screenHeight step cellSize) {
            SDL_RenderDrawLine(ptr, 0, j, config.screenWidth, j)
        }
    }
    override fun dispose() {
        SDL_DestroyRenderer(ptr)
    }
}