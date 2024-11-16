package com.tecknobit.ametistaengine.deviceinfo

import com.tecknobit.ametistaengine.configuration.targets.TargetConfig
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * The **DeviceInfo** class represents the information about the current device where the application is running
 *
 * @param uniqueIdentifier The unique identifier of the user
 * @param brand The brand of the device
 * @param model The model of the device
 * @param os The operating system of the device
 * @param osVersion The operating system version of the device
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see TargetConfig
 */
open class DeviceInfo(
    val uniqueIdentifier: String,
    val brand: String,
    val model: String,
    val os: String,
    val osVersion: String
) {

    companion object {

        /**
         * **DEVICE_KEY** -> the key for the "device" value
         */
        const val DEVICE_KEY = "device"

        /**
         * **DEVICE_IDENTIFIER_KEY** -> the key for the "device_id" value
         */
        private const val DEVICE_IDENTIFIER_KEY = "device_id"

        /**
         * **BRAND_KEY** -> the key for the "brand" value
         */
        private const val BRAND_KEY = "brand"

        /**
         * **MODEL_KEY** -> the key for the "model" value
         */
        private const val MODEL_KEY = "model"

        /**
         * **OS_KEY** -> the key for the "os" value
         */
        private const val OS_KEY = "os"

        /**
         * **OS_VERSION_KEY** -> the key for the "os_version" value
         */
        private const val OS_VERSION_KEY = "os_version"

    }

    /**
     * Method to transform the device information as payload for the requests
     *
     * @return the device info formated as [JsonObject]
     */
    open fun toPayload(): JsonObject {
        return buildJsonObject {
            put(DEVICE_IDENTIFIER_KEY, uniqueIdentifier)
            put(BRAND_KEY, brand)
            put(MODEL_KEY, model)
            put(OS_KEY, os)
            put(OS_VERSION_KEY, osVersion)
        }
    }
    
}