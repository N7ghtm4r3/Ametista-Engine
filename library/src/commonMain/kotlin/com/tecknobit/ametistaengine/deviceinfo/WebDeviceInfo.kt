package com.tecknobit.ametistaengine.deviceinfo

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class WebDeviceInfo(
    uniqueIdentifier: String,
    brand: String,
    model: String,
    os: String,
    osVersion: String,
    val browser: String,
    val browserVersion: String,
) : DeviceInfo(
    uniqueIdentifier = uniqueIdentifier,
    brand = brand,
    model = model,
    os = os,
    osVersion = osVersion
) {

    companion object {

        private const val BROWSER_KEY = "browser"

        private const val BROWSER_VERSION_KEY = "browser_version"

    }

    override fun toPayload(): JsonObject {
        val deviceInfo = super.toPayload().toMutableMap()
        deviceInfo[BROWSER_KEY] = JsonPrimitive(browser)
        deviceInfo[BROWSER_VERSION_KEY] = JsonPrimitive(browserVersion)
        return JsonObject(deviceInfo)
    }

}