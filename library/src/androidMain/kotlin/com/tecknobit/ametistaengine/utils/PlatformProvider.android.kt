package com.tecknobit.ametistaengine.utils

import com.tecknobit.ametistaengine.utils.EngineConfiguration.Platform
import com.tecknobit.ametistaengine.utils.EngineConfiguration.Platform.ANDROID

actual fun currentPlatform(): Platform {
    return ANDROID
}