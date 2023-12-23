package prism.engine.common.utils

import platform.SDL2.SDL_LOG_PRIORITY_INFO
import platform.SDL2.SDL_LogCategory
import platform.SDL2.SDL_LogMessage

fun debugLog(message: String) {
    if (Platform.isDebugBinary) {
        SDL_LogMessage(SDL_LogCategory.SDL_LOG_CATEGORY_APPLICATION.ordinal, SDL_LOG_PRIORITY_INFO, message)
    }
}