package com.tecknobit.ametistaengine

import com.tecknobit.mole.KMole
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class EngineManager private constructor() {

    companion object {

        const val FILES_AMETISTA_CONFIG_PATHNAME = "files/ametista.config"

        val engineManager: EngineManager by lazy { EngineManager() }

    }

    private lateinit var configuration: EngineConfiguration

    val host: String
        get() = configuration.host

    val serverSecret: String
        get() = configuration.serverSecret

    val applicationId: String
        get() = configuration.applicationId

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
        configuration = Json.decodeFromString(configData.decodeToString())
        val kMole = KMole.kMole
        println(kMole.uniqueIdentifier)
        println(kMole.brand)
        println(kMole.model)
        println(kMole.os)
        println(kMole.osVersion)
    }

    @Serializable
    internal data class EngineConfiguration(
        val host: String,
        @SerialName("server_secret")
        val serverSecret: String,
        @SerialName("application_id")
        val applicationId: String
    )

}