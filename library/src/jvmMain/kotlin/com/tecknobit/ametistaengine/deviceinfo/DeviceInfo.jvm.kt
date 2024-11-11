package com.tecknobit.ametistaengine.deviceinfo

import oshi.SystemInfo

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DeviceInfo private actual constructor() {

    actual companion object {

        actual val deviceInfo: DeviceInfo by lazy { DeviceInfo() }

        private const val TO_BE_FILLED_BY_O_E_M = "To Be Filled By O.E.M."

        private const val CUSTOM_BUILD = "Custom Build"

    }

    private val info = SystemInfo()

    private val operatingSystem = info.operatingSystem

    private val computerInfo = info.hardware.computerSystem

    actual val uniqueIdentifier: String = computerInfo.baseboard.serialNumber

    actual val brand: String = operatingSystem.manufacturer

    actual val model: String

    actual val os: String = operatingSystem.family

    actual val osVersion: String = operatingSystem.versionInfo.version

    init {
        val guard = computerInfo.model
        model = if (guard == TO_BE_FILLED_BY_O_E_M)
            CUSTOM_BUILD
        else
            guard
    }

}