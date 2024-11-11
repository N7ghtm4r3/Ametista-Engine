package com.tecknobit.ametistaengine.utils

import com.tecknobit.ametistaengine.utils.EngineConfiguration.Platform
import com.tecknobit.ametistaengine.utils.EngineConfiguration.Platform.IOS

actual fun currentPlatform(): Platform {
    return IOS
}