package com.tecknobit.ametistaengine.deviceinfo

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

open class DeviceInfo(
    val uniqueIdentifier: String,
    val brand: String,
    val model: String,
    val os: String,
    val osVersion: String
) {

    companion object {

        const val DEVICE_KEY = "device"

        private const val DEVICE_IDENTIFIER_KEY = "device_id"

        private const val BRAND_KEY = "brand"

        private const val MODEL_KEY = "model"

        private const val OS_KEY = "os"

        private const val OS_VERSION_KEY = "os_version"

    }

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