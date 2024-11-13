package com.tecknobit.ametistaengine.deviceinfo

import kotlinx.browser.window

private const val UKNOWN = "uknown"

actual fun provideDeviceInfo(): DeviceInfo {
    val userAgent = window.navigator.userAgent
    val result = parseUserAgent(
        userAgent = userAgent
    )
    return WebDeviceInfo(
        uniqueIdentifier = userAgent,
        brand = result.device.vendor.safeValue(),
        model = result.device.model.safeValue(),
        os = result.os.name.safeValue(),
        osVersion = result.os.version.safeValue(),
        browser = result.browser.name.safeValue(),
        browserVersion = result.browser.version.safeValue()
    )
}

private fun String?.safeValue(): String {
    return if (this != null)
        this
    else
        UKNOWN
}