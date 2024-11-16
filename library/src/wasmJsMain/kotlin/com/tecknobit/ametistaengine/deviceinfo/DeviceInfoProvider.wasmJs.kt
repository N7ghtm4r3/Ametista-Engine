package com.tecknobit.ametistaengine.deviceinfo

import kotlinx.browser.window

/**
 * **UKNOWN** -> the uknown value to use when the specific information has not found
 */
private const val UKNOWN = "uknown"

/**
 * Method to provide the current device information
 *
 * @return the device information as [WebDeviceInfo]
 */
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

/**
 * Method to use a null-safe value
 *
 * @return the found value or the [UKNOWN] value as [String]
 */
private fun String?.safeValue(): String {
    return if (this != null)
        this
    else
        UKNOWN
}