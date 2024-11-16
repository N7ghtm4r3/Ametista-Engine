package com.tecknobit.ametistaengine.deviceinfo

import com.tecknobit.ametistaengine.configuration.targets.TargetConfig
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * The **WebDeviceInfo** class represents the information about the current browser (and related device) where the application
 * is running
 *
 * @param uniqueIdentifier The unique identifier of the user
 * @param brand The brand of the device
 * @param model The model of the device
 * @param os The operating system of the device
 * @param osVersion The operating system version of the device
 * @param browser The browser where the web-app is running
 * @param browserVersion The version of the browser
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see TargetConfig
 */
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

        /**
         * **BROWSER_KEY** -> the key for the "browser" value
         */
        private const val BROWSER_KEY = "browser"

        /**
         * **BROWSER_VERSION_KEY** -> the key for the "browser_version" value
         */
        private const val BROWSER_VERSION_KEY = "browser_version"

    }

    /**
     * Method to transform the device information as payload for the requests
     *
     * @return the device info formated as [JsonObject]
     */
    override fun toPayload(): JsonObject {
        val deviceInfo = super.toPayload().toMutableMap()
        deviceInfo[BROWSER_KEY] = JsonPrimitive(browser)
        deviceInfo[BROWSER_VERSION_KEY] = JsonPrimitive(browserVersion)
        return JsonObject(deviceInfo)
    }

}