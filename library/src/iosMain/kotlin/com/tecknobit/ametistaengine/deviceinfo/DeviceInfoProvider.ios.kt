package com.tecknobit.ametistaengine.deviceinfo

import com.devx.kdeviceinfo.DeviceInfoXState

/**
 * **APPLE_BRAND** -> constant "apple" value
 */
private const val APPLE_BRAND = "Apple"

/**
 * Method to provide the current device information
 *
 * @return the device information as [DeviceInfo]
 */
actual fun provideDeviceInfo(): DeviceInfo {
    val deviceInfo = DeviceInfoXState().iosInfo
    val brand: String = APPLE_BRAND
    val model: String = deviceInfo.name
    val os: String = deviceInfo.systemName
    val osVersion: String = deviceInfo.systemVersion
    val uniqueIdentifier = brand + "_" + model + "_" + os + "_" + osVersion
    return DeviceInfo(
        uniqueIdentifier = uniqueIdentifier,
        brand = brand,
        model = model,
        os = os,
        osVersion = osVersion
    )
}