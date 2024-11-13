package com.tecknobit.ametistaengine.utils

import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Platform
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Platform.ANDROID

actual fun currentPlatform(): Platform {
    return ANDROID
}