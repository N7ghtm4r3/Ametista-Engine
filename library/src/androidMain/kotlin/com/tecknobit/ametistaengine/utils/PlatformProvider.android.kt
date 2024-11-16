package com.tecknobit.ametistaengine.utils

import com.tecknobit.ametistaengine.configuration.EngineConfiguration
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Platform
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Platform.ANDROID

/**
 * Method to get the current platform where the [com.tecknobit.ametistaengine.AmetistaEngine] is running
 *
 * @return the current platform as [EngineConfiguration.Platform]
 */
actual fun currentPlatform(): Platform {
    return ANDROID
}