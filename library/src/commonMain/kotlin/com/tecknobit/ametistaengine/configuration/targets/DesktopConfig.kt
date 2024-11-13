package com.tecknobit.ametistaengine.configuration.targets

import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.APP_VERSION_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DesktopConfig(
    @SerialName(APP_VERSION_KEY)
    override val appVersion: String? = null
) : TargetConfig(
    appVersion = appVersion
)