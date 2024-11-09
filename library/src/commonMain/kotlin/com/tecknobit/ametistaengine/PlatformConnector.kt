package com.tecknobit.ametistaengine

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