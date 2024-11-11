package com.tecknobit.ametistaengine.utils

import com.tecknobit.ametistaengine.utils.EngineConfiguration.Platform
import com.tecknobit.ametistaengine.utils.EngineConfiguration.Platform.DESKTOP

actual fun currentPlatform(): Platform {
    return DESKTOP
}