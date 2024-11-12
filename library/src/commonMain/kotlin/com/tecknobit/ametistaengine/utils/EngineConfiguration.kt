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

        const val APP_VERSION_KEY = "app_version"

        const val PERFORMANCE_ANALYTIC_TYPE_KEY = "performance_analytic_type"

        const val PLATFORM_KEY = "platform"

        const val LAUNCH_TIME_KEY = "launch_time"

        const val PERFORMANCE_ANALYTICS_ENDPOINT = "/performance_analytics"

    }

    enum class Platform {

        ANDROID,

        IOS,

        DESKTOP,

        WEB

    }

    enum class PerformanceAnalyticType {

        LAUNCH_TIME,

        NETWORK_REQUESTS,

        TOTAL_ISSUES,

        ISSUES_PER_SESSION

    }

}