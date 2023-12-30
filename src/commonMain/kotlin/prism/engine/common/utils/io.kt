package prism.engine.common.utils

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
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
internal expect val resourcePath: String?

object IO {

    /**
     * Reads the contents of a text file and returns it as a string.
     *
     * @param filePath The path to the text file to be read.
     * @return The content of the text file as a string.
     * @throws IllegalArgumentException If the file cannot be opened or an error occurs during reading.
     */
    fun readText(filePath: String): String {
        val returnBuffer = StringBuilder()

        // Open the file for reading
        val file = fopen(filePath, "r") ?:
        throw IllegalArgumentException("Cannot open input file $filePath")

        try {
            memScoped {
                val readBufferLength = 64 * 1024
                val buffer = allocArray<ByteVar>(readBufferLength)

                // Read the file line by line into the StringBuilder
                var line = fgets(buffer, readBufferLength, file)?.toKString()
                while (line != null) {
                    returnBuffer.append(line)
                    line = fgets(buffer, readBufferLength, file)?.toKString()
                }
            }
        } finally {
            // Close the file
            fclose(file)
        }

        return returnBuffer.toString()
    }

    /**
     * Writes the specified text to a text file, creating the file if it does not exist.
     *
     * @param filePath The path to the text file to be written.
     * @param text The text to be written to the file.
     * @throws IllegalArgumentException If the file cannot be opened or an error occurs during writing.
     */
    fun writeAllText(filePath: String, text: String) {
        // Open the file for writing
        val file = fopen(filePath, "a") ?:
        throw IllegalArgumentException("Cannot open output file $filePath")

        try {
            memScoped {
                // Write the text to the file
                if (fputs(text, file) == EOF) throw Error("File write error")
            }
        } finally {
            // Close the file
            fclose(file)
        }
    }

    /**
     * Writes a list of strings to a text file, with an optional line ending.
     *
     * @param filePath The path to the text file to be written.
     * @param lines The list of strings to be written to the file.
     * @param lineEnding The line ending to be used between each line (default is "\n").
     * @throws IllegalArgumentException If the file cannot be opened or an error occurs during writing.
     */
    fun writeAllLines(filePath: String, lines: List<String>, lineEnding: String = "\n") {
        // Open the file for writing
        val file = fopen(filePath, "a") ?:
        throw IllegalArgumentException("Cannot open output file $filePath")

        try {
            memScoped {
                // Write each line to the file with the specified line ending
                lines.forEach {
                    if (fputs(it + lineEnding, file) == EOF) {
                        throw Error("File write error")
                    }
                }
            }
        } catch (e: Throwable) {
            // Handle exceptions and print an error message
            println("Error: ${e.message}")
        } finally {
            // Close the file
            fclose(file)
        }
    }
}
