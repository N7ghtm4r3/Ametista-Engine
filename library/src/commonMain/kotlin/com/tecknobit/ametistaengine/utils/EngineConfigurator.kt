package com.tecknobit.ametistaengine.utils

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class EngineConfigurator {

    companion object {

        fun getInstance(): EngineConfigurator

    }

    fun fetchConfiguration(): EngineConfiguration

}