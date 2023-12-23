package prism.engine

import platform.SDL2.*
import kotlinx.cinterop.*
import prism.engine.common.input.Keyboard
import prism.engine.scene.Scene
import kotlin.reflect.KClass
import prism.engine.graphics.Renderer
import prism.engine.graphics.Window

/**
 * Represents the delay between frames in milliseconds.
 */
private const val FRAME_DELAY: UInt = 16u

/**
 * PrismLoader function to initialize and run a Prism game.
 *
 * @param game The instance of the Prism game to run.
 */
fun PrismLoader(game: Prism) {
    game.run()
}

/**
 * Default empty scene used to initialize Prism if no custom scene is provided.
 * Implements the [Scene] interface with empty implementations for render, update, and dispose methods.
 */
private class EmptyScene : Scene {
    override fun render(renderer: Renderer) {}
    override fun update() {}
    override fun dispose() {}
}

/**
 * Abstract class representing a Prism game.
 *
 * @property config The configuration for initialising the Prism game.
 */
abstract class Prism(config: PrismConfigurator) {

    // Static window
    companion object {
        lateinit var window: Window
            private set
    }
    // Private properties
    private val renderer: Renderer
    private var isRunning = true

    /**
     * Initializes the SDL2 and Image subsystems, creates a window and renderer, and sets up the game configuration.
     *
     * @throws RuntimeException if SDL2 fails to initialize or create necessary components.
     */
    init {
        require(SDL_Init(config.initialisationMode) == 0) {
            "ERROR: SDL2 failed to initialise! ${SDL_GetError()?.toKString()}"
        }
        if (Platform.isDebugBinary) SDL_LogMessage(SDL_LogCategory.SDL_LOG_CATEGORY_APPLICATION.ordinal, SDL_LOG_PRIORITY_INFO, "SDL2 Initialised successfully.")

        window = Window(config)
        if (Platform.isDebugBinary) SDL_LogMessage(SDL_LogCategory.SDL_LOG_CATEGORY_APPLICATION.ordinal, SDL_LOG_PRIORITY_INFO, "Window created successfully.")

        if (config.fullScreen) SDL_SetWindowFullscreen(window.getWindowPointer(), SDL_WINDOW_FULLSCREEN_DESKTOP)
        SDL_SetHint(config.rendererHint, config.hintValue)

        renderer = Renderer(window, config)
        if (Platform.isDebugBinary) SDL_LogMessage(SDL_LogCategory.SDL_LOG_CATEGORY_APPLICATION.ordinal, SDL_LOG_PRIORITY_INFO, "Renderer created successfully.")

        require(IMG_Init((IMG_INIT_PNG or IMG_INIT_JPG).toInt()) != 0) {
            "ERROR: SDL2 failed to initialise Image submodule! ${SDL_GetError()?.toKString()}"
        }
        if (Platform.isDebugBinary) SDL_LogMessage(SDL_LogCategory.SDL_LOG_CATEGORY_APPLICATION.ordinal, SDL_LOG_PRIORITY_INFO, "SDL2 Image Subsystem initialised successfully.")
    }

    // Scene management
    @PublishedApi internal val scenes: HashMap<KClass<out Scene>, Lazy<Scene>> = HashMap()
    @PublishedApi internal var currentScene: Lazy<Scene> = lazy { EmptyScene() }

    /**
     * Adds a scene of the specified type to the game.
     *
     * @param scene The scene to add.
     * @throws RuntimeException if a scene of the same type is already registered.
     */
    inline fun <reified T : Scene> addScene(scene: Lazy<T>) {
        scenes.takeIf { !it.containsKey(T::class) }
            ?: throw RuntimeException("Scene of type ${T::class} is already registered. Use removeScene first to replace the scene.")
        scenes[T::class] = scene
    }

    /**
     * Sets the current scene to the one of the specified type.
     *
     * @throws RuntimeException if the specified scene type is not registered.
     */
    inline fun <reified T : Scene> setScene() {
        currentScene = scenes[T::class] ?: throw RuntimeException("Scene of type ${T::class} is not registered. Use addScene first to register the scene.")
    }

    /**
     * Removes a scene of the specified type.
     * This does not call dispose()
     *
     * @param type The type of scene to remove.
     * @throws RuntimeException if the scene of the specified type is not registered.
     */
    inline fun <Type : Scene> removeScene(type: KClass<Type>) = scenes.remove(type)
        ?: throw RuntimeException("Scene of type $type is not registered. Unable to remove.")

    /**
     * Removes and calls dispose() function of a scene of the specified type.
     *
     * @param type The type of scene to dispose.
     * @throws NoSuchElementException if the scene of the specified type is not registered.
     */
    inline fun <Type : Scene> disposeScene(type: KClass<Type>) {
        val lazyScene = scenes[type]
        if (lazyScene != null && lazyScene.isInitialized()) {
            lazyScene.value.dispose()
        }
        scenes.remove(type) ?: throw NoSuchElementException("Scene of type $type is not registered. Unable to remove and dispose.")
    }

    /**
     * Checks if a scene of the specified type is registered.
     *
     * @return True if the scene is registered, false otherwise.
     */
    inline fun <reified T : Scene> hasScene(): Boolean {
        return scenes.contains(T::class)
    }

    /**
     * Pulls SDL events, such as keyboard input and window close events.
     */
    private fun pullEvents() {
        memScoped {
            val event = alloc<SDL_Event>()
            while (SDL_PollEvent(event.ptr) != 0) {
                when (event.type) {
                    SDL_QUIT -> quit()
                    SDL_KEYDOWN -> {
                        if (Keyboard.isKeyJustPressed(SDL_SCANCODE_ESCAPE)) {
                            quit()
                        }
                    }
                }
            }
        }
    }

    /**
     * Runs the main game loop, rendering scenes, updating input, and handling events until the game is stopped.
     */
    internal fun run() {
        while (isRunning) {
            currentScene.value.render(renderer)
            Keyboard.update()
            pullEvents()
            currentScene.value.update()
            renderer.present()
            SDL_Delay(FRAME_DELAY)
        }
    }

    /**
     * Stops the game loop and performs cleanup tasks.
     */
    private fun quit() {
        isRunning = false
        IMG_Quit()
        renderer.dispose()
        window.dispose()
        scenes.values.forEach { it.value.dispose() }
        scenes.clear()
        SDL_Quit()
    }
}