package com.tecknobit.ametistaengine.deviceinfo

import com.tecknobit.kinfo.KInfoState

/**
 * Method to provide the current device information
 *
 * @return the device information as [WebDeviceInfo]
 */
actual fun provideDeviceInfo(): DeviceInfo {
    val webInfo = KInfoState().webInfo
    val device = webInfo.device
    val os = webInfo.os
    val browser = webInfo.browser
    return WebDeviceInfo(
        uniqueIdentifier = webInfo.userAgent,
        brand = device.vendor,
        model = device.model,
        os = os.name,
        osVersion = os.version,
        browser = browser.name,
        browserVersion = browser.version
    )
}