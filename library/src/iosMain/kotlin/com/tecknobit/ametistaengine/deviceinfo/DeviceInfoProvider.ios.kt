package com.tecknobit.ametistaengine.deviceinfo

import com.tecknobit.kinfo.KInfoState

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
    val iosInfo = KInfoState().iosInfo
    val brand: String = APPLE_BRAND
    val model: String = iosInfo.name
    val os: String = iosInfo.systemName
    val osVersion: String = iosInfo.systemVersion
    return DeviceInfo(
        uniqueIdentifier = brand + "_" + model + "_" + os + "_" + osVersion,
        brand = brand,
        model = model,
        os = os,
        osVersion = osVersion
    )
}