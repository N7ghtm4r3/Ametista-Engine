package com.tecknobit.ametistaengine.utils

class EngineManager private constructor() {

    companion object {

        const val ENGINE_CONFIGURATION_FILE = "ametista.config"

        val engineManager: EngineManager
            get() = EngineManager()

    }

    init {
        val engineConfigurator = EngineConfigurator.getInstance()
        engineConfigurator.fetchConfiguration()
    }

}