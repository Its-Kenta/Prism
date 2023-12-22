package prism.engine.interfaces

/**
 * Interface representing an object that can be disposed of, releasing any acquired resources.
 * Implementing classes should define the disposal logic in the [dispose] method.
 */
interface Disposable {
    /**
     * Disposes of the object, releasing any resources it may hold.
     * This method should be called when the object is no longer needed to free up resources.
     */
    fun dispose()
}