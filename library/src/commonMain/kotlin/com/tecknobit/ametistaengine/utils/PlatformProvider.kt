package com.tecknobit.ametistaengine.utils

import com.tecknobit.ametistaengine.configuration.EngineConfiguration

/**
 * Method to get the current platform where the [com.tecknobit.ametistaengine.AmetistaEngine] is running
 *
 * @return the current platform as [EngineConfiguration.Platform]
 */
expect fun currentPlatform(): EngineConfiguration.Platform