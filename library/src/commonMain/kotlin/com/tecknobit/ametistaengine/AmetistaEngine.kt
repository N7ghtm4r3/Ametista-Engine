package com.tecknobit.ametistaengine

import com.tecknobit.ametistaengine.configuration.EngineConfiguration
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.ENDPOINT_URL
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Platform
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Platform.ANDROID
import com.tecknobit.ametistaengine.deviceinfo.DeviceInfo
import com.tecknobit.ametistaengine.deviceinfo.provideDeviceInfo
import com.tecknobit.ametistaengine.utils.EngineRequester
import com.tecknobit.ametistaengine.utils.catchIssue
import com.tecknobit.ametistaengine.utils.currentPlatform
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import kotlinx.serialization.json.Json

/**
 * The **AmetistaEngine** class is the core component of the Ametista system.
 * It collects performance data and tracks issues to send to your backend instance for analysis
 *
 * @param platform The platform from the stats collected and the issues caught are sent
 *
 * @author N7ghtm4r3 - Tecknobit
 */
class AmetistaEngine private constructor(
    private val platform: Platform
) {

    companion object {

        /**
         * **FILES_AMETISTA_CONFIG_PATHNAME** -> the pathname of the configuration file located in the common resources
         * folder
         */
        const val FILES_AMETISTA_CONFIG_PATHNAME = "files/ametista.config"

        /**
         * **ANDROID_LOCALHOST_VALUE** -> the value of the localhost for the **Android** emulators
         */
        private const val ANDROID_LOCALHOST_VALUE = "10.0.2.2"

        /**
         * **LOCALHOST_VALUE** -> the "localhost" value
         */
        private const val LOCALHOST_VALUE = "localhost"

        /**
         * **LOCALHOST_ADDRESS_VALUE** -> the "127.0.0.1" address value
         */
        private const val LOCALHOST_ADDRESS_VALUE = "127.0.0.1"

        /**
         * **RESPONSE_KEY** -> the response key
         */
        private const val RESPONSE_KEY = "response"

        /**
         * **STATUS_KEY** -> the status key
         */
        private const val STATUS_KEY = "status"

        /**
         * **REQUEST_FAILED** -> the failed response status
         */
        private const val REQUEST_FAILED = "FAILED"

        /**
         * **initializationTimestamp** -> the current timestamp when the engine has been initialized invoking
         * the [intake] method
         */
        private var initializationTimestamp: Long = -1

        /**
         * **ametistaEngine** -> the singleton instance of the engine to use in the clients application, will be
         * returned the specific instance for each target platform
         */
        val ametistaEngine: AmetistaEngine by lazy {
            AmetistaEngine(
                platform = currentPlatform()
            )
        }

        /**
         * Method to start the [AmetistaEngine] session initializing the [initializationTimestamp] and register the
         * [catchIssue] handler
         */
        fun intake() {
            initializationTimestamp = Clock.System.now().toEpochMilliseconds()
            catchIssue()
        }

    }

    /**
     * **deviceInfo** -> the current device information
     */
    private val deviceInfo: DeviceInfo = provideDeviceInfo()

    /**
     * **engineRequester** -> the requester used to send the statistics to the backend
     */
    private lateinit var engineRequester: EngineRequester

    /**
     * **configurationMutex** -> the mutex used to wait the configuration loaded before execute any operation
     */
    private val configurationMutex = Mutex(
        locked = true
    )

    /**
     * **host** -> the host address where send the stats and performance data collected
     */
    private lateinit var host: String

    /**
     * **serverSecret** -> the server secret value used as authentication method to validate the requests of the Engine
     */
    private lateinit var serverSecret: String

    /**
     * **applicationId** -> the identifier of the current application managed by the Engine
     */
    private lateinit var applicationId: String

    /**
     * **appVersion** -> the current application version managed by the Engine
     */
    private var appVersion: String? = null

    /**
     * **configurationLoaded** -> whether the configuration has been loaded correctly
     */
    private var configurationLoaded: Boolean = false

    /**
     * Method to initialize the Engine with the configuration data and the flags available
     *
     * @param configPath is the configuration path where the config file is located
     * @param host The host address value of the collector server
     * @param serverSecret The server secret of the personal Ametista backend instance
     * @param bypassSslValidation - Whether bypass the SSL certificates validation, this for example when is a self-signed the
     * certificate USE WITH CAUTION
     * @param applicationId The identifier of the application to collect its data
     * @param debugMode concerns whether the Engine must send the requests but the server must not collect as real, this is the
     * use-case of a not-production environment
     */
    fun fireUp(
        configPath: String,
        host: String,
        serverSecret: String,
        applicationId: String,
        bypassSslValidation: Boolean = false,
        debugMode: Boolean,
    ) {
        val configData = SystemFileSystem.source(Path(configPath)).buffered().readByteArray()
        fireUp(
            configData = configData,
            host = host,
            serverSecret = serverSecret,
            applicationId = applicationId,
            bypassSslValidation = bypassSslValidation,
            debugMode = debugMode
        )
    }

    /**
     * Method to initialize the Engine with the configuration data and the flags available
     *
     * @param configData are the config data as [ByteArray]
     *
     * @param host The host address value of the collector server
     * @param serverSecret The server secret of the personal Ametista backend instance
     * @param bypassSslValidation - Whether bypass the SSL certificates validation, this for example when is a self-signed the
     * certificate USE WITH CAUTION
     * @param applicationId The identifier of the application to collect its data
     * @param debugMode concerns whether the Engine must send the requests but the server must not collect as real, this is the
     * use-case of a not-production environment
     */
    fun fireUp(
        configData: ByteArray,
        host: String,
        serverSecret: String,
        applicationId: String,
        bypassSslValidation: Boolean = false,
        debugMode: Boolean,
    ) {
        if (initializationTimestamp < 0)
            throw IllegalArgumentException("To correctly start the engine you must invoke AmetistaEngine.intake method first")
        try {
            loadConfiguration(
                configData = configData,
                host = host,
                serverSecret = serverSecret,
                applicationId = applicationId,
                bypassSslValidation = bypassSslValidation,
                debugMode = debugMode
            )
            notifyAppLaunch()
        } catch (e: Exception) {
            throwInvalidConfiguration()
        }
    }

    /**
     * Method to load the configuration serializing the data in the [EngineConfiguration] data class
     * and initializing the [host], [serverSecret], [applicationId] and [appVersion] instances
     *
     * @param configData the data from instantiate the [EngineConfiguration]
     * @param host The host address value of the collector server
     * @param serverSecret The server secret of the personal Ametista backend instance
     * @param bypassSslValidation - Whether bypass the SSL certificates validation, this for example when is a self-signed the
     * certificate USE WITH CAUTION
     * @param applicationId The identifier of the application to collect its data
     * @param debugMode concerns whether the Engine must send the requests but the server must not collect as real, this is the
     * use-case of a not-production environment
     */
    private fun loadConfiguration(
        configData: ByteArray,
        host: String,
        serverSecret: String,
        applicationId: String,
        bypassSslValidation: Boolean = false,
        debugMode: Boolean,
    ) {
        val configuration: EngineConfiguration = Json.decodeFromString(configData.decodeToString())
        appVersion = getAppVersion(
            configuration = configuration
        )
        this.host = host
        if (platform == ANDROID)
            formatHostForAndroid()
        this.serverSecret = serverSecret
        this.applicationId = applicationId
        configurationLoaded = isValidConfiguration()
        if (!configurationLoaded)
            throwInvalidConfiguration()
        engineRequester = EngineRequester(
            host = "$host$ENDPOINT_URL$applicationId",
            debugMode = debugMode,
            byPassSSLValidation = bypassSslValidation,
            serverSecret = serverSecret,
            appVersion = appVersion!!,
            platform = platform
        )
    }

    /**
     * Method to format the [host] value in the correct localhost value address for the **Android** emulators
     * with the [ANDROID_LOCALHOST_VALUE]
     */
    private fun formatHostForAndroid() {
        host = host
            .replace(LOCALHOST_VALUE, ANDROID_LOCALHOST_VALUE)
            .replace(LOCALHOST_ADDRESS_VALUE, ANDROID_LOCALHOST_VALUE)
    }

    /**
     * Method to get from the engine configuration the application version, this method check first if the version is
     * specific for the current platform where the application is running, if not found will be used the generic one
     *
     * @param configuration from search the application version
     *
     * @return application version as [String]
     */
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

    /**
     * Method to check whether the configuration is correctly loaded
     *
     * @return whether the configuration is correctly loaded as [Boolean]
     */
    private fun isValidConfiguration(): Boolean {
        return appVersion != null && isValidHost() && serverSecret.isNotBlank() && applicationId.isNotBlank()
    }

    /**
     * Method to check whether the [host] value is correctly formatted
     *
     * @return whether the [host] value is correctly formatted as [Boolean]
     */
    private fun isValidHost(): Boolean {
        val regex = "^(https?|ftp|file|mailto|data|ws|wss)://([a-zA-Z0-9\\-.]+)(:\\d+)?(/\\S*)?$"
        return regex.toRegex().matches(host)
    }

    /**
     * Method to execute an action after the configuration has been loaded. The [configurationMutex] synchronize
     * the access locking it waiting the configuration loading
     *
     * @param action to execute when the configuration has been loaded
     */
    fun execAfterConfigurationLoaded(
        action: () -> Unit
    ) {
        MainScope().launch {
            configurationMutex.withLock {
                action.invoke()
            }
        }
    }

    /**
     * Method to connect the platform where the application is currently running.
     *
     * Look at the documentation [here](https://github.com/N7ghtm4r3/Ametista-Engine?tab=readme-ov-file#connection-procedure)
     */
    fun connectPlatform() {
        checkConfigurationValidity()
        engineRequester.connectPlatform()
    }

    /**
     * Method to notify the application launch and send the related value to the server.
     *
     * The [configurationMutex] is unlocked by this method
     */
    private fun notifyAppLaunch() {
        configurationMutex.unlock()
        checkConfigurationValidity()
        engineRequester.notifyAppLaunch(
            initializationTimestamp = initializationTimestamp
        )
    }

    /**
     * Method to request the count of a network request sent by the application
     *
     * Look at the documentation [here](https://github.com/N7ghtm4r3/Ametista-Engine?tab=readme-ov-file#network-requests-count-if-needed)
     */
    fun notifyNetworkRequest() {
        checkConfigurationValidity()
        engineRequester.notifyNetworkRequest()
    }

    /**
     * Method to send the crash report of an issue occurred during the runtime of the application
     *
     * @param issue from fetch its stack trace to send as report
     */
    fun notifyIssue(
        issue: Throwable
    ) {
        notifyIssue(
            issue = issue.stackTraceToString()
        )
    }

    /**
     * Method to send the crash report of an issue occurred during the runtime of the application
     *
     * @param issue details to create the report
     */
    fun notifyIssue(
        issue: String
    ) {
        checkConfigurationValidity()
        engineRequester.notifyIssue(
            issue = issue,
            deviceInfo = deviceInfo
        )
    }

    /**
     * Method to check the validity of the configuration loaded
     */
    private fun checkConfigurationValidity() {
        if (!configurationLoaded)
            throwInvalidConfiguration()
    }

    /**
     * Method to throw an error if the configuration is not valid, if the error is related to the [appVersion] will
     * be instead used a specific message
     */
    private fun throwInvalidConfiguration() {
        val message = if (appVersion != null)
            "Invalid configuration, check it before running the engine"
        else
            "Invalid app app version, set one or specific for each target before running the engine\nSee more at: https://github.com/N7ghtm4r3/Ametista-Engine?tab=readme-ov-file#connection-procedure"
        throw IllegalArgumentException(message)
    }

}