package com.tecknobit.ametistaengine

import com.tecknobit.ametistaengine.configuration.EngineConfiguration
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.APP_VERSION_KEY
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.ENDPOINT_URL
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.ISSUES_ENDPOINT
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.ISSUE_KEY
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.IS_DEBUG_MODE_KEY
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.LAUNCH_TIME_KEY
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.PERFORMANCE_ANALYTICS_ENDPOINT
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.PERFORMANCE_ANALYTIC_TYPE_KEY
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.PLATFORM_KEY
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.SERVER_SECRET_KEY
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.PerformanceAnalyticType.LAUNCH_TIME
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.PerformanceAnalyticType.NETWORK_REQUESTS
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Platform
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Platform.ANDROID
import com.tecknobit.ametistaengine.deviceinfo.DeviceInfo
import com.tecknobit.ametistaengine.deviceinfo.DeviceInfo.Companion.DEVICE_KEY
import com.tecknobit.ametistaengine.deviceinfo.provideDeviceInfo
import com.tecknobit.ametistaengine.utils.catchIssue
import com.tecknobit.ametistaengine.utils.currentPlatform
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.HttpMethod.Companion.Put
import io.ktor.http.HttpStatusCode.Companion.OK
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import kotlinx.serialization.json.*

class AmetistaEngine private constructor(
    private val platform: Platform
) {

    companion object {

        // TODO: WARN ABOUT HIDE THE FILE FOR EXAMPLE IN THE COMMIT OR PUBLIC PUBLISHING 
        const val FILES_AMETISTA_CONFIG_PATHNAME = "files/ametista.config"

        private const val ANDROID_LOCALHOST_VALUE = "10.0.2.2"

        private const val LOCALHOST_VALUE = "localhost"

        private const val LOCALHOST_ADDRESS_VALUE = "127.0.0.1"

        private const val RESPONSE_KEY = "response"

        private const val STATUS_KEY = "status"

        private const val REQUEST_FAILED = "FAILED"

        private var initializationTimestamp: Long = -1

        val ametistaEngine: AmetistaEngine by lazy {
            AmetistaEngine(
                platform = currentPlatform()
            )
        }

        fun intake() {
            initializationTimestamp = Clock.System.now().toEpochMilliseconds()
            catchIssue()
        }

    }

    private val deviceInfo: DeviceInfo = provideDeviceInfo()

    private val ktorClient = HttpClient()

    private val mutex = Mutex()

    private val configurationMutex = Mutex(
        locked = true
    )

    private lateinit var host: String

    private lateinit var serverSecret: String

    private lateinit var applicationId: String

    private var appVersion: String? = null

    private var configurationLoaded: Boolean = false

    private var loggingEnabled: Boolean = false

    private var debugMode: Boolean = false

    // TODO: ADD THE POSSIBILITY TO FLAG IF THE CONFIGURATION IS IN DEBUG_MODE TO AVOID SEND OF STATS AND ISSUES 
    fun fireUp(
        configPath: String,
        loggingEnabled: Boolean = false,
        debugMode: Boolean
    ) {
        val configData = SystemFileSystem.source(Path(configPath)).buffered().readByteArray()
        fireUp(
            configData = configData,
            loggingEnabled = loggingEnabled,
            debugMode = debugMode
        )
    }

    // TODO: WARN IN THE DOCU ABOUT THIS USAGE EXAMPLE WITH val bytes = Res.readBytes("files/ametista.config")
    // TODO: LaunchedEffect(Unit) {
    //        val ametistaEngine = AmetistaEngine.ametistaEngine
    //        ametistaEngine.fireUp(
    //            configData = Res.readBytes(FILES_AMETISTA_CONFIG_PATHNAME),
    //            loggingEnabled = true,
    //            debugMode = true
    //        )
    //    }
    fun fireUp(
        configData: ByteArray,
        loggingEnabled: Boolean = false,
        debugMode: Boolean
    ) {
        if (initializationTimestamp < 0)
            throw IllegalArgumentException("To correctly start the engine you must invoke AmetistaEngine.intake method first")
        try {
            loadConfiguration(
                configData = configData
            )
            setLogging(
                enabled = loggingEnabled
            )
            notifyAppLaunch()
        } catch (e: Exception) {
            throwInvalidConfiguration()
        }
        this.debugMode = debugMode
    }

    private fun loadConfiguration(
        configData: ByteArray
    ) {
        val configuration: EngineConfiguration = Json.decodeFromString(configData.decodeToString())
        appVersion = getAppVersion(
            configuration = configuration
        )
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

    private fun getAppVersion(
        configuration: EngineConfiguration
    ): String? {
        val targetAppVersion = configuration.getTargetConfiguration(
            platform = platform
        )?.appVersion?.ifEmpty { null }
        return if (targetAppVersion == null)
            configuration.appVersion?.ifEmpty { null }
        else
            targetAppVersion
    }

    private fun isValidConfiguration(): Boolean {
        return appVersion != null && isValidHost() && serverSecret.isNotBlank() && applicationId.isNotBlank()
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

    fun execAfterConfigurationLoaded(
        action: () -> Unit
    ) {
        MainScope().launch {
            configurationMutex.withLock {
                action.invoke()
            }
        }
    }

    fun connectPlatform() {
        sendRequest(
            method = Put
        )
    }

    private fun notifyAppLaunch() {
        configurationMutex.unlock()
        val launchTime = Clock.System.now().toEpochMilliseconds() - initializationTimestamp
        sendRequest(
            method = Put,
            endpoint = PERFORMANCE_ANALYTICS_ENDPOINT,
            parameters = mapOf(
                APP_VERSION_KEY to appVersion!!,
                PERFORMANCE_ANALYTIC_TYPE_KEY to LAUNCH_TIME
            ),
            payload = buildJsonObject {
                put(LAUNCH_TIME_KEY, launchTime)
            }
        )
    }

    fun notifyNetworkRequest() {
        sendRequest(
            method = Put,
            endpoint = PERFORMANCE_ANALYTICS_ENDPOINT,
            parameters = mapOf(
                APP_VERSION_KEY to appVersion!!,
                PERFORMANCE_ANALYTIC_TYPE_KEY to NETWORK_REQUESTS
            )
        )
    }

    fun notifyIssue(
        issue: Throwable
    ) {
        notifyIssue(
            issue = issue.stackTraceToString()
        )
    }

    fun notifyIssue(
        issue: String
    ) {
        sendRequest(
            method = Put,
            endpoint = ISSUES_ENDPOINT,
            parameters = mapOf(
                APP_VERSION_KEY to appVersion!!
            ),
            payload = buildJsonObject {
                put(ISSUE_KEY, issue)
                put(DEVICE_KEY, deviceInfo.toPayload())
            }
        )
    }

    private fun sendRequest(
        method: HttpMethod,
        endpoint: String = "",
        parameters: Map<String, Any> = emptyMap(),
        payload: JsonObject? = null
    ) {
        checkConfigurationValidity()
        MainScope().launch {
            mutex.withLock {
                val response = ktorClient.request(
                    urlString = "$host$ENDPOINT_URL$applicationId$endpoint"
                ) {
                    this.method = method
                    url {
                        parameters {
                            parameter(PLATFORM_KEY, platform)
                            parameter(IS_DEBUG_MODE_KEY, debugMode)
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
        val message = if (appVersion != null)
            "Invalid configuration, check it before running the engine"
        else
        // TODO: ADD THE REFERENCE LINK TO THE DOCU
            "Invalid app app version, set one or specific for each target before running the engine\nSee more at:"
        throw IllegalArgumentException(message)
    }

}