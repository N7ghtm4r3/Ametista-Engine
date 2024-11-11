package com.tecknobit.ametistaengine.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EngineConfiguration(
    val host: String,
    @SerialName(SERVER_SECRET_KEY)
    val serverSecret: String,
    @SerialName(APPLICATION_IDENTIFIER_KEY)
    val applicationId: String
) {

    companion object {

        const val SERVER_SECRET_KEY = "server_secret"

        const val APPLICATION_IDENTIFIER_KEY = "application_id"

        const val PLATFORM_KEY = "platform"

    }

    enum class Platform {

        ANDROID,

        IOS,

        DESKTOP,

        WEB

    }

}