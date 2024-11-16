package com.tecknobit.ametistaengine.configuration

import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Platform.*
import com.tecknobit.ametistaengine.configuration.targets.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The **EngineConfiguration** class is the container of the details of the configuration to use during the Engine's
 * session
 *
 * @param host The host address value of the collector server
 * @param serverSecret The server secret of the personal Ametista backend instance
 * @param applicationId The identifier of the application to collect its data
 * @param appVersion The current application version managed by the Engine
 * @param androidConfig The specific configuration for the Android target
 * @param iosConfig The specific configuration for the iOs target
 * @param desktopConfig The specific configuration for the desktop target
 * @param webConfig The specific configuration for the web target
 *
 * @author N7ghtm4r3 - Tecknobit
 */
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

        /**
         * **SERVER_SECRET_KEY** -> the key for the "server_secret" value
         */
        const val SERVER_SECRET_KEY = "server_secret"

        /**
         * **IS_DEBUG_MODE_KEY** -> the key for the "is_debug_mode" value
         */
        const val IS_DEBUG_MODE_KEY = "is_debug_mode"

        /**
         * **APPLICATION_IDENTIFIER_KEY** -> the key for the "application_id" value
         */
        const val APPLICATION_IDENTIFIER_KEY = "application_id"

        /**
         * **APP_VERSION_KEY** -> the key for the "app_version" value
         */
        const val APP_VERSION_KEY = "app_version"

        /**
         * **PERFORMANCE_ANALYTIC_TYPE_KEY** -> the key for the "performance_analytic_type" value
         */
        const val PERFORMANCE_ANALYTIC_TYPE_KEY = "performance_analytic_type"

        /**
         * **PLATFORM_KEY** -> the key for the "platform" value
         */
        const val PLATFORM_KEY = "platform"

        /**
         * **LAUNCH_TIME_KEY** -> the key for the "launch_time" value
         */
        const val LAUNCH_TIME_KEY = "launch_time"

        /**
         * **ISSUE_KEY** -> the key for the "issue" value
         */
        const val ISSUE_KEY = "issue"

        /**
         * **ENDPOINT_URL** -> the base endpoint of the collector server
         */
        const val ENDPOINT_URL = "/api/v1/applications/"

        /**
         * **PERFORMANCE_ANALYTICS_ENDPOINT** -> the endpoint of the performance analytics data
         */
        const val PERFORMANCE_ANALYTICS_ENDPOINT = "/performance_analytics"

        /**
         * **ISSUES_ENDPOINT** -> the endpoint of the issues data
         */
        const val ISSUES_ENDPOINT = "/issues"

    }

    /**
     * **Platform** -> the platform target values
     */
    enum class Platform {

        /**
         * **ANDROID** -> the Android target
         */
        ANDROID,

        /**
         * **IOS** -> the iOs target
         */
        IOS,

        /**
         * **DESKTOP** -> the desktop target
         */
        DESKTOP,

        /**
         * **WEB** -> the web target
         */
        WEB

    }

    /**
     * **PerformanceAnalyticType** -> the analytics available
     */
    enum class PerformanceAnalyticType {

        /**
         * **LAUNCH_TIME** -> Inherent measurement of application startup time
         */
        LAUNCH_TIME,

        /**
         * **NETWORK_REQUESTS** -> Inherent measure of the number of HTTP requests executed by each application daily
         */
        NETWORK_REQUESTS,

        /**
         * **TOTAL_ISSUES** -> Inherent measurement of the number of crashes or problems encountered while using the application
         */
        TOTAL_ISSUES,

        /**
         * **ISSUES_PER_SESSION** -> Inherent measurement of the average number of crashes or issues encountered while using the application for a single session
         */
        ISSUES_PER_SESSION

    }

    /**
     * Method to get the correct [TargetConfig] for based on the [platform]
     *
     * @param platform The platform from fetch the related configuration
     *
     * @return the specific configuration as [TargetConfig]
     */
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