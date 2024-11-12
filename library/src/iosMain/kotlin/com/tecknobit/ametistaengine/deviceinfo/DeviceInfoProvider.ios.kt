package com.tecknobit.ametistaengine.deviceinfo

import com.devx.kdeviceinfo.DeviceInfoXState

private const val APPLE_BRAND = "Apple"

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