package com.tecknobit.ametistaengine.utils

import com.tecknobit.ametistaengine.utils.EngineConfiguration.Platform
import com.tecknobit.ametistaengine.utils.EngineConfiguration.Platform.WEB

actual fun currentPlatform(): Platform {
    return WEB
}