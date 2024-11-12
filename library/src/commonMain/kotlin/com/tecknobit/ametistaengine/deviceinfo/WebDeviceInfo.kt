package com.tecknobit.ametistaengine.deviceinfo

import kotlinx.serialization.json.JsonObject

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
        val payload = super.toPayload()
        payload.plus(
            pairs = listOf(
                Pair(BROWSER_KEY, browser),
                Pair(BROWSER_VERSION_KEY, browserVersion)
            )
        )
        return payload
    }

}