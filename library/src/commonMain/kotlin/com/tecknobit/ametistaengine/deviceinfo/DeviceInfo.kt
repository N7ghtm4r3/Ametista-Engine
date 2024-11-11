package com.tecknobit.ametistaengine.deviceinfo

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class DeviceInfo private constructor() {

    companion object {

        val deviceInfo: DeviceInfo

    }

    val uniqueIdentifier: String

    val brand: String

    val model: String

    val os: String

    val osVersion: String

}