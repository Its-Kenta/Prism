package prism.engine

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.cinterop.toKString
import platform.posix.*

/**
 * Returns the absolute path to the 'resources' folder located at the same location as your executable.
 *
 * This property provides the path to the 'resources' folder associated with your executable.
 * Ensure that a folder named 'resources' is present in the same directory as your executable.
 *
 * @return The absolute path to the 'resources' folder, or null if it doesn't exist or cannot be determined.
 */
@OptIn(ExperimentalForeignApi::class)
actual inline val resourcePath: String?
    get() {
        return runCatching {
            val maxPathLength = 4096
            val pid = getpid() // Get the process ID (PID) of the current process
            val buffer = ByteArray(maxPathLength) // Create a buffer to store the path
            val procPath = "/proc/$pid/exe" // Build the path to the '/proc' directory for the process

            // Use the realpath function to get the canonicalised absolute pathname of the executable
            if (realpath(procPath, buffer.refTo(0)) == null) throw RuntimeException("Failed to retrieve executable path.")
            val executablePath = buffer.toKString() // Convert the buffer, which contains the absolute path, to a Kotlin string

            // Find the last '/' character to extract the directory path
            val lastSlashIndex = executablePath.lastIndexOf('/')
            if (lastSlashIndex == -1) throw RuntimeException("Invalid executable path format: $executablePath.")

            val executableDir = executablePath.substring(0, lastSlashIndex + 1) // Extract the directory portion of the executable path, including the trailing slash.
            val resourceDir = executableDir + PrismConfigurator.resourceLocator // Return the directory path of the executable file along with the "resources" subdirectory.

            // Check if the "resources" directory exists
            if (access(resourceDir, F_OK) != 0) throw RuntimeException("The 'resources' directory does not exist at: $resourceDir, unable to load resources.")

            return resourceDir

        }.onFailure {
            println("RuntimeException: ${it.message}")
            return null
        }.getOrNull()
    }