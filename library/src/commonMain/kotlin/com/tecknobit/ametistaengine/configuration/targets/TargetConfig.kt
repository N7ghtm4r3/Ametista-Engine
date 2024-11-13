package com.tecknobit.ametistaengine.configuration.targets

import kotlinx.serialization.Serializable

@Serializable
abstract class TargetConfig(
    open val appVersion: String? = null
)