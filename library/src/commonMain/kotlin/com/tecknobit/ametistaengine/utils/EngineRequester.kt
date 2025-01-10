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
import kotlinx.serialization.json.*

/**
 * **debugMode** -> whether the Engine must send the requests but the server must not collect as real, this is the
 * use-case of a not-production environment
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
    connectionErrorMessage = "",
) {

    private val engineScope = CoroutineScope(Dispatchers.Default)

    private val headers = buildMap {
        put(SERVER_SECRET_KEY, serverSecret)
    }

    private val defQueryParameters = buildJsonObject {
        put(PLATFORM_KEY, platform.name)
        put(IS_DEBUG_MODE_KEY, debugMode)
    }

    fun connectPlatform() {
        engineScope.launch {
            execPut(
                endpoint = "",
                headers = headers,
                query = defQueryParameters
            )
        }
    }

    fun notifyAppLaunch(
        initializationTimestamp: Long,
    ) {
        val launchTime = Clock.System.now().toEpochMilliseconds() - initializationTimestamp
        val query = mergeExtraQueryParameters(
            keys = listOf(APP_VERSION_KEY, PERFORMANCE_ANALYTIC_TYPE_KEY),
            values = listOf(appVersion, LAUNCH_TIME)
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

    fun notifyNetworkRequest() {
        val query = mergeExtraQueryParameters(
            keys = listOf(APP_VERSION_KEY, PERFORMANCE_ANALYTIC_TYPE_KEY),
            values = listOf(appVersion, NETWORK_REQUESTS)
        )
        engineScope.launch {
            execPut(
                endpoint = PERFORMANCE_ANALYTICS_ENDPOINT,
                headers = headers,
                query = query
            )
        }
    }

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

    private fun mergeExtraQueryParameters(
        keys: List<String>,
        values: List<*>,
    ): JsonObject {
        val defMap = defQueryParameters.toMutableMap()
        keys.forEachIndexed { index, key ->
            defMap[key] = Json.encodeToJsonElement(values[index])
        }
        return JsonObject(defMap)
    }

}