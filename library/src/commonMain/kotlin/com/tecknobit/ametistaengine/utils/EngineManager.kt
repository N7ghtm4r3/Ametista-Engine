package com.tecknobit.ametistaengine.utils

import com.tecknobit.ametistaengine.deviceinfo.DeviceInfo
import com.tecknobit.ametistaengine.utils.EngineConfiguration.Companion.PLATFORM_KEY
import com.tecknobit.ametistaengine.utils.EngineConfiguration.Companion.SERVER_SECRET_KEY
import com.tecknobit.ametistaengine.utils.EngineConfiguration.Platform
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import kotlinx.serialization.json.Json

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

    }

    private val deviceInfo: DeviceInfo = DeviceInfo.deviceInfo

    private val ktorClient = HttpClient()

    private lateinit var host: String

    private lateinit var serverSecret: String

    private lateinit var applicationId: String

    private var configurationLoaded: Boolean = false

    fun init(
        configPath: String
    ) {
        val configData = SystemFileSystem.source(Path(configPath)).buffered().readByteArray()
        init(
            configData = configData
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
        configData: ByteArray
    ) {
        try {
            val configuration: EngineConfiguration = Json.decodeFromString(configData.decodeToString())
            host = configuration.host
            serverSecret = configuration.serverSecret
            applicationId = configuration.applicationId
            configurationLoaded = isValidConfiguration()
            if (!configurationLoaded)
                throwInvalidConfiguration()
        } catch (e: Exception) {
            throwInvalidConfiguration()
        }
    }

    private fun isValidConfiguration(): Boolean {
        return isValidHost() && serverSecret.isNotBlank() && applicationId.isNotBlank()
    }

    private fun isValidHost(): Boolean {
        val regex = "^(https?|ftp|file|mailto|data|ws|wss)://([a-zA-Z0-9\\-.]+)(:\\d+)?(/\\S*)?$"
        return regex.toRegex().matches(host)
    }

    fun connectPlatform() {
        checkConfigurationValidity()
        MainScope().launch {
            async {
                val request = ktorClient.put(
                    urlString = "$host/$applicationId",
                ) {
                    parameter(PLATFORM_KEY, platform.name)
                    headers {
                        append(SERVER_SECRET_KEY, serverSecret)
                    }
                }
                println(request.bodyAsText())
            }.await()
        }
    }

    fun noticeAppLaunch() {


    }

    fun noticeNetworkRequest() {

    }

    fun noticeIssue() {

    }

    private fun checkConfigurationValidity() {
        if (!configurationLoaded)
            throwInvalidConfiguration()
    }

    private fun throwInvalidConfiguration() {
        throw IllegalArgumentException("Invalid configuration, check it before running the engine")
    }

}