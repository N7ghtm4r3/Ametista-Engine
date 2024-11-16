package com.tecknobit.ametistaengine.configuration.targets

import kotlinx.serialization.Serializable

/**
 * The **TargetConfig** class is the container of the details of the specific target configuration
 *
 * @param appVersion The current application version managed by the Engine
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Serializable
abstract class TargetConfig(
    open val appVersion: String? = null
)