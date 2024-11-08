package com.tecknobit.ametistaengine.utils

import com.tecknobit.ametistaengine.utils.EngineManager.Companion.ENGINE_CONFIGURATION_FILE

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class EngineConfigurator {

    actual fun fetchConfiguration(): EngineConfiguration {
        val inputStream = object {}.javaClass.classLoader?.getResourceAsStream(ENGINE_CONFIGURATION_FILE)
        return inputStream!!.bufferedReader().use { it.readText() }
    }

}