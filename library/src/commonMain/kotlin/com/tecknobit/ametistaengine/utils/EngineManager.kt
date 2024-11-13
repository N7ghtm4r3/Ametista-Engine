package com.tecknobit.ametistaengine.utils

import com.tecknobit.ametistaengine.deviceinfo.DeviceInfo
import com.tecknobit.ametistaengine.deviceinfo.DeviceInfo.Companion.DEVICE_KEY
import com.tecknobit.ametistaengine.deviceinfo.provideDeviceInfo
import com.tecknobit.ametistaengine.utils.EngineConfiguration.Companion.APP_VERSION_KEY
import com.tecknobit.ametistaengine.utils.EngineConfiguration.Companion.ENDPOINT_URL
import com.tecknobit.ametistaengine.utils.EngineConfiguration.Companion.ISSUES_ENDPOINT
import com.tecknobit.ametistaengine.utils.EngineConfiguration.Companion.ISSUE_KEY
import com.tecknobit.ametistaengine.utils.EngineConfiguration.Companion.LAUNCH_TIME_KEY
import com.tecknobit.ametistaengine.utils.EngineConfiguration.Companion.PERFORMANCE_ANALYTICS_ENDPOINT
import com.tecknobit.ametistaengine.utils.EngineConfiguration.Companion.PERFORMANCE_ANALYTIC_TYPE_KEY
import com.tecknobit.ametistaengine.utils.EngineConfiguration.Companion.PLATFORM_KEY
import com.tecknobit.ametistaengine.utils.EngineConfiguration.Companion.SERVER_SECRET_KEY
import com.tecknobit.ametistaengine.utils.EngineConfiguration.PerformanceAnalyticType.LAUNCH_TIME
import com.tecknobit.ametistaengine.utils.EngineConfiguration.PerformanceAnalyticType.NETWORK_REQUESTS
import com.tecknobit.ametistaengine.utils.EngineConfiguration.Platform
import com.tecknobit.ametistaengine.utils.EngineConfiguration.Platform.ANDROID
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.OK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import kotlinx.serialization.json.*

class EngineManager private constructor(
    private val platform: Platform
) {

    companion object {

        // TODO: WARN ABOUT HIDE THE FILE FOR EXAMPLE IN THE COMMIT OR PUBLIC PUBLISHING 
        const val FILES_AMETISTA_CONFIG_PATHNAME = "files/ametista.config"

        val engineManager: EngineManager by lazy {
            EngineManager(
                platform = currentPlatform()
            )
        }

        private const val ANDROID_LOCALHOST_VALUE = "10.0.2.2"

        private const val LOCALHOST_VALUE = "localhost"

        private const val LOCALHOST_ADDRESS_VALUE = "127.0.0.1"

        private const val RESPONSE_KEY = "response"

        private const val STATUS_KEY = "status"

        private const val REQUEST_FAILED = "FAILED"

    }

    private val deviceInfo: DeviceInfo = provideDeviceInfo()

    private val ktorClient = HttpClient()

    private val mutex = Mutex()

    private lateinit var host: String

    private lateinit var serverSecret: String

    private lateinit var applicationId: String

    private var configurationLoaded: Boolean = false

    private lateinit var requestBuilder: HttpRequestBuilder.() -> Unit

    private var loggingEnabled: Boolean = false

    private var initializationTimestamp: Long = 0

    // TODO: ADD THE POSSIBILITY TO FLAG IF THE CONFIGURATION IS IN DEBUG_MODE TO AVOID SEND OF STATS AND ISSUES 
    fun init(
        configPath: String,
        loggingEnabled: Boolean = false
    ) {
        val configData = SystemFileSystem.source(Path(configPath)).buffered().readByteArray()
        init(
            configData = configData,
            loggingEnabled = loggingEnabled
        )
    }

    // TODO: WARN IN THE DOCU ABOUT THIS USAGE EXAMPLE WITH val bytes = Res.readBytes("files/ametista.config")
    // TODO: val engineManager = EngineManager.engineManager
    //        var text by remember { mutableStateOf("loading...") }
    //        LaunchedEffect(
    //            Unit
    //        ) {
    //            engineManager.init(
    //                configData = Res.readBytes(FILES_AMETISTA_CONFIG_PATHNAME)
    //            )
    //            text = engineManager.host
    //        }
    //        Text(
    //            text = text
    //        )
    fun init(
        configData: ByteArray,
        loggingEnabled: Boolean = false
    ) {
        try {
            loadConfiguration(
                configData = configData
            )
            setLogging(
                enabled = loggingEnabled
            )
            requestBuilder = {
                parameter(PLATFORM_KEY, platform.name)
                headers {
                    append(SERVER_SECRET_KEY, serverSecret)
                }
            }
            initializationTimestamp = Clock.System.now().toEpochMilliseconds()
        } catch (e: Exception) {
            throwInvalidConfiguration()
        }
    }

    private fun loadConfiguration(
        configData: ByteArray
    ) {
        val configuration: EngineConfiguration = Json.decodeFromString(configData.decodeToString())
        host = configuration.host
        if (platform == ANDROID)
            formatHostForAndroid()
        serverSecret = configuration.serverSecret
        applicationId = configuration.applicationId
        configurationLoaded = isValidConfiguration()
        if (!configurationLoaded)
            throwInvalidConfiguration()
    }

    private fun formatHostForAndroid() {
        host = host
            .replace(LOCALHOST_VALUE, ANDROID_LOCALHOST_VALUE)
            .replace(LOCALHOST_ADDRESS_VALUE, ANDROID_LOCALHOST_VALUE)
    }

    private fun isValidConfiguration(): Boolean {
        return isValidHost() && serverSecret.isNotBlank() && applicationId.isNotBlank()
    }

    private fun isValidHost(): Boolean {
        val regex = "^(https?|ftp|file|mailto|data|ws|wss)://([a-zA-Z0-9\\-.]+)(:\\d+)?(/\\S*)?$"
        return regex.toRegex().matches(host)
    }

    private fun setLogging(
        enabled: Boolean
    ) {
        loggingEnabled = enabled
        if (enabled)
            Napier.base(DebugAntilog())
    }

    fun connectPlatform() {
        sendRequest(
            method = HttpMethod.Put,
            requestUrl = "$host$ENDPOINT_URL$applicationId"
        )
    }

    fun notifyAppLaunch() {
        // TODO: TO USE THE REAL VALUE
        val appVersion = "1.0.0"
        val launchTime = Clock.System.now().toEpochMilliseconds() - initializationTimestamp
        sendRequest(
            method = HttpMethod.Put,
            requestUrl = "$host$ENDPOINT_URL$applicationId$PERFORMANCE_ANALYTICS_ENDPOINT",
            parameters = mapOf(
                APP_VERSION_KEY to appVersion,
                PERFORMANCE_ANALYTIC_TYPE_KEY to LAUNCH_TIME
            ),
            payload = buildJsonObject {
                put(LAUNCH_TIME_KEY, launchTime)
            }
        )
    }

    fun notifyNetworkRequest() {
        // TODO: TO USE THE REAL VALUE
        val appVersion = "1.0.0"
        sendRequest(
            method = HttpMethod.Put,
            requestUrl = "$host$ENDPOINT_URL$applicationId$PERFORMANCE_ANALYTICS_ENDPOINT",
            parameters = mapOf(
                APP_VERSION_KEY to appVersion,
                PERFORMANCE_ANALYTIC_TYPE_KEY to NETWORK_REQUESTS
            )
        )
    }

    fun notifyIssue(
        issue: Exception
    ) {
        // TODO: TO USE THE REAL VALUE
        val appVersion = "1.0.0"
        sendRequest(
            method = HttpMethod.Put,
            requestUrl = "$host$ENDPOINT_URL$applicationId$ISSUES_ENDPOINT",
            parameters = mapOf(
                APP_VERSION_KEY to appVersion
            ),
            payload = buildJsonObject {
                put(ISSUE_KEY, issue.message)
                put(DEVICE_KEY, deviceInfo.toPayload())
            }
        )
    }

    private fun sendRequest(
        method: HttpMethod,
        requestUrl: String,
        parameters: Map<String, Any> = emptyMap(),
        payload: JsonObject? = null
    ) {
        checkConfigurationValidity()
        CoroutineScope(Dispatchers.Default).launch {
            mutex.withLock {
                val response = ktorClient.request(
                    urlString = requestUrl
                ) {
                    this.method = method
                    url {
                        parameters {
                            parameter(PLATFORM_KEY, platform)
                            parameters.entries.forEach { parameter ->
                                parameter(parameter.key, parameter.value)
                            }
                        }
                        headers {
                            append(SERVER_SECRET_KEY, serverSecret)
                            payload?.let {
                                append(HttpHeaders.ContentType, ContentType.Application.Json)
                            }
                        }
                        payload?.let { payload ->
                            setBody(payload.toString())
                        }
                    }
                }
                execRequest(
                    response = response
                )
            }
        }
    }

    private suspend fun execRequest(
        response: HttpResponse
    ) {
        if (loggingEnabled) {
            val responseText = response.bodyAsText()
            if (response.status == OK) {
                val jResponse = Json.parseToJsonElement(responseText).jsonObject
                val status = jResponse[STATUS_KEY]!!.jsonPrimitive.content
                val data = jResponse[RESPONSE_KEY]!!.jsonPrimitive.content
                if (status == REQUEST_FAILED)
                    Napier.e(data)
                else
                    Napier.i(data)
            } else
                Napier.e(response.status.description)
        }
    }

    private fun checkConfigurationValidity() {
        if (!configurationLoaded)
            throwInvalidConfiguration()
    }

    private fun throwInvalidConfiguration() {
        throw IllegalArgumentException("Invalid configuration, check it before running the engine")
    }

}