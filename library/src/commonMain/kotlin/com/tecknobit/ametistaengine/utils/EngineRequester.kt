package com.tecknobit.ametistaengine.utils

import com.tecknobit.ametistaengine.configuration.EngineConfiguration
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.APP_VERSION_KEY
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.ISSUES_ENDPOINT
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.ISSUE_KEY
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.IS_DEBUG_MODE_KEY
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.LAUNCH_TIME_KEY
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.PERFORMANCE_ANALYTICS_ENDPOINT
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.PERFORMANCE_ANALYTIC_TYPE_KEY
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.PLATFORM_KEY
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.PerformanceAnalyticType.LAUNCH_TIME
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.PerformanceAnalyticType.NETWORK_REQUESTS
import com.tecknobit.ametistaengine.deviceinfo.DeviceInfo
import com.tecknobit.ametistaengine.deviceinfo.DeviceInfo.Companion.DEVICE_KEY
import com.tecknobit.equinoxcore.helpers.SERVER_SECRET_KEY
import com.tecknobit.equinoxcore.network.Requester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * The **EngineRequester** is used to send the data collected and tracked issues to your backend instance for analysis
 *
 * @param host The host address where send the stats and performance data collected
 * @param debugMode Whether the Engine must send the requests but the server must not collect as real, this is the
 *  use-case of a not-production environment
 * @param byPassSSLValidation Whether bypass the **SSL** certificates validation, this for example
 * @param serverSecret The server secret value used as authentication method to validate the requests of the Engine
 * @param appVersion The current application version managed by the Engine
 * @param platform The platform from the stats collected and the issues caught are sent
 *
 * @author N7ghtm4r3 - Tecknobit
 */
internal class EngineRequester(
    host: String,
    debugMode: Boolean,
    byPassSSLValidation: Boolean,
    private val serverSecret: String,
    private val appVersion: String,
    platform: EngineConfiguration.Platform,
) : Requester(
    host = host,
    debugMode = debugMode,
    byPassSSLValidation = byPassSSLValidation,
    connectionErrorMessage = "Something goes wrong",
) {

    /**
     * `engineScope` is used to send the request to the backend
     */
    private val engineScope = CoroutineScope(Dispatchers.Default)

    /**
     * `headers` of the requests
     */
    private val headers = buildMap {
        put(SERVER_SECRET_KEY, serverSecret)
    }

    /**
     * `defQueryParameters` the default query parameters to use in the requests
     */
    private val defQueryParameters = buildJsonObject {
        put(PLATFORM_KEY, Json.parseToJsonElement(platform.name))
        put(IS_DEBUG_MODE_KEY, debugMode)
    }

    /**
     * Method to send the request to connect the platform where the application is currently running.
     *
     * Look at the documentation [here](https://github.com/N7ghtm4r3/Ametista-Engine?tab=readme-ov-file#connection-procedure)
     */
    fun connectPlatform() {
        engineScope.launch {
            execPut(
                endpoint = "",
                headers = headers,
                query = defQueryParameters
            )
        }
    }

    /**
     * Method to  send the request to notify the application launch and send the related value to the server.
     *
     * @param initializationTimestamp The current timestamp when the engine has been initialized invoking
     * the [com.tecknobit.ametistaengine.AmetistaEngine.intake] method
     */
    fun notifyAppLaunch(
        initializationTimestamp: Long,
    ) {
        val launchTime = Clock.System.now().toEpochMilliseconds() - initializationTimestamp
        val query = mergeExtraQueryParameters(
            keys = listOf(APP_VERSION_KEY, PERFORMANCE_ANALYTIC_TYPE_KEY),
            values = listOf(appVersion, LAUNCH_TIME.name)
        )
        val payload = buildJsonObject {
            put(LAUNCH_TIME_KEY, launchTime)
        }
        engineScope.launch {
            execPut(
                endpoint = PERFORMANCE_ANALYTICS_ENDPOINT,
                headers = headers,
                query = query,
                payload = payload
            )
        }
    }

    /**
     * Method to send the request to count the network requests sent by the application
     *
     * Look at the documentation [here](https://github.com/N7ghtm4r3/Ametista-Engine?tab=readme-ov-file#network-requests-count-if-needed)
     */
    fun notifyNetworkRequest() {
        val query = mergeExtraQueryParameters(
            keys = listOf(APP_VERSION_KEY, PERFORMANCE_ANALYTIC_TYPE_KEY),
            values = listOf(appVersion, NETWORK_REQUESTS.name)
        )
        engineScope.launch {
            execPut(
                endpoint = PERFORMANCE_ANALYTICS_ENDPOINT,
                headers = headers,
                query = query
            )
        }
    }

    /**
     * Method to send the request to notify crash report of an issue occurred during the runtime of the application
     *
     * @param issue Details to create the report
     * @param deviceInfo The current device information
     */
    fun notifyIssue(
        issue: String,
        deviceInfo: DeviceInfo,
    ) {
        val query = mergeExtraQueryParameters(
            keys = listOf(APP_VERSION_KEY),
            values = listOf(appVersion)
        )
        val payload = buildJsonObject {
            put(ISSUE_KEY, issue)
            put(DEVICE_KEY, deviceInfo.toPayload())
        }
        engineScope.launch {
            execPut(
                endpoint = ISSUES_ENDPOINT,
                headers = headers,
                query = query,
                payload = payload
            )
        }
    }

    /**
     * Method to merge extra query parameters for a request additive of the [defQueryParameters]
     *
     * @param keys The keys of the parameters to add
     * @param values The values of the query parameter to apply
     *
     * @return the complete query for the request as [JsonObject]
     */
    private fun mergeExtraQueryParameters(
        keys: List<String>,
        values: List<String>,
    ): JsonObject {
        val defMap = defQueryParameters.toMutableMap()
        keys.forEachIndexed { index, key ->
            defMap[key] = Json.decodeFromString(values[index])
        }
        return JsonObject(defMap)
    }

}