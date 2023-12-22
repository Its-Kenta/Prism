package prism.engine.scene

import prism.engine.interfaces.Disposable
import prism.engine.graphics.Renderer

/**
 * Interface representing a scene in a Prism game.
 * A scene is responsible for rendering graphics, updating game logic, and managing resources.
 * It extends the [Disposable] interface to ensure proper cleanup when a scene is no longer needed.
 */
interface Scene : Disposable {
    /**
     * Renders the graphics for the scene using the specified [renderer].
     *
     * @param renderer The SDL renderer used for rendering graphics.
     */
    fun render(renderer: Renderer)

    /**
     * Updates the game logic for the scene.
     * This method is called in each iteration of the game loop.
     */
    fun update()
}