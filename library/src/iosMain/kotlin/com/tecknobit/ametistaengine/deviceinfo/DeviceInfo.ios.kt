package com.tecknobit.ametistaengine.deviceinfo

import com.devx.kdeviceinfo.DeviceInfoXState

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DeviceInfo {

    actual companion object {

        actual val deviceInfo: DeviceInfo by lazy { DeviceInfo() }

        private val device = DeviceInfoXState().iosInfo

        private const val APPLE_BRAND = "Apple"

    }

    actual val uniqueIdentifier: String

    actual val brand: String = APPLE_BRAND

    actual val model: String = device.name

    actual val os: String = device.systemName

    actual val osVersion: String = device.systemVersion

    init {
        uniqueIdentifier = brand + "_" + model + "_" + os + "_" + osVersion
    }

}