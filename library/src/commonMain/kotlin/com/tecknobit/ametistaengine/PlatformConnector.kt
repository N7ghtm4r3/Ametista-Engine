package com.tecknobit.ametistaengine

import com.tecknobit.ametistaengine.utils.EngineManager

class PlatformConnector {

    enum class Platform {

        ANDROID,

        IOS,

        DESKTOP,

        WEB

    }

    init {
        val configuration = EngineManager.engineManager
    }

}