package com.tecknobit.ametistaengine.configuration.targets

import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.APP_VERSION_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The **IosConfig** class is the container of the configuration details for the iOs target
 *
 * @param appVersion The current application version managed by the Engine
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see TargetConfig
 */
@Serializable
data class IosConfig(
    @SerialName(APP_VERSION_KEY)
    override val appVersion: String? = null
) : TargetConfig(
    appVersion = appVersion
)