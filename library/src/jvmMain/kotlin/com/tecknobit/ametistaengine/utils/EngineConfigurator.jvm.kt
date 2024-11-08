package com.tecknobit.ametistaengine.utils

import com.tecknobit.ametistaengine.utils.EngineManager.Companion.ENGINE_CONFIGURATION_FILE
import com.tecknobit.apimanager.apis.ResourcesUtils

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class EngineConfigurator {

    actual companion object {

        actual fun getInstance(): EngineConfigurator {
            return EngineConfigurator()
        }

    }

    actual fun fetchConfiguration(): EngineConfiguration {
        val resourcesUtils = ResourcesUtils(this::class.java)
        val configuration = resourcesUtils.getResourceContent(ENGINE_CONFIGURATION_FILE)
        return EngineConfiguration(
            host = "",
            serverSecret = "",
            applicationId = ""
        )
    }

}