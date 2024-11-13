package com.tecknobit.ametistaengine.configuration

import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Platform.*
import com.tecknobit.ametistaengine.configuration.targets.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EngineConfiguration(
    val host: String,
    @SerialName(SERVER_SECRET_KEY)
    val serverSecret: String,
    @SerialName(APPLICATION_IDENTIFIER_KEY)
    val applicationId: String,
    @SerialName(APP_VERSION_KEY)
    val appVersion: String? = null,
    @SerialName("android")
    val androidConfig: AndroidConfig? = null,
    @SerialName("ios")
    val iosConfig: IosConfig? = null,
    @SerialName("desktop")
    val desktopConfig: DesktopConfig? = null,
    @SerialName("web")
    val webConfig: WebConfig? = null
) {

    companion object {

        const val SERVER_SECRET_KEY = "server_secret"

        const val IS_DEBUG_MODE_KEY = "is_debug_mode"

        const val APPLICATION_IDENTIFIER_KEY = "application_id"

        const val APP_VERSION_KEY = "app_version"

        const val PERFORMANCE_ANALYTIC_TYPE_KEY = "performance_analytic_type"

        const val PLATFORM_KEY = "platform"

        const val LAUNCH_TIME_KEY = "launch_time"

        const val ISSUE_KEY = "issue"

        const val ENDPOINT_URL = "/api/v1/applications/"

        const val PERFORMANCE_ANALYTICS_ENDPOINT = "/performance_analytics"

        const val ISSUES_ENDPOINT = "/issues"

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

    fun getTargetConfiguration(
        platform: Platform
    ): TargetConfig? {
        return when (platform) {
            ANDROID -> androidConfig
            IOS -> iosConfig
            DESKTOP -> desktopConfig
            WEB -> webConfig
        }
    }

}