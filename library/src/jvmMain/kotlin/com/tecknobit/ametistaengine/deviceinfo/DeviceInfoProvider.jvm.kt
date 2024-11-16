package com.tecknobit.ametistaengine.deviceinfo

import oshi.SystemInfo

/**
 * **TO_BE_FILLED_BY_O_E_M** -> constant "To Be Filled By O.E.M." value
 */
private const val TO_BE_FILLED_BY_O_E_M = "To Be Filled By O.E.M."

/**
 * **CUSTOM_BUILD** -> constant "Custom Build" value
 */
private const val CUSTOM_BUILD = "Custom Build"

/**
 * Method to provide the current device information
 *
 * @return the device information as [DeviceInfo]
 */
actual fun provideDeviceInfo(): DeviceInfo {
    val info = SystemInfo()
    val operatingSystem = info.operatingSystem
    val computerInfo = info.hardware.computerSystem
    val uniqueIdentifier: String = computerInfo.baseboard.serialNumber
    val brand: String = operatingSystem.manufacturer
    val guard = computerInfo.model
    val model: String = if (guard == TO_BE_FILLED_BY_O_E_M)
        CUSTOM_BUILD
    else
        guard
    val os: String = operatingSystem.family
    val osVersion: String = operatingSystem.versionInfo.version
    return DeviceInfo(
        uniqueIdentifier = uniqueIdentifier,
        brand = brand,
        model = model,
        os = os,
        osVersion = osVersion
    )
}