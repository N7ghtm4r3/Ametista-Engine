package com.tecknobit.ametistaengine.deviceinfo

import com.tecknobit.kinfo.KInfoState
import com.tecknobit.kinfo.model.desktop.DesktopInfo.Companion.whenIsToBeFilledByOEM

/**
 * `CUSTOM_BUILD` constant "Custom Build" value
 */
private const val CUSTOM_BUILD = "Custom Build"

/**
 * Method to provide the current device information
 *
 * @return the device information as [DeviceInfo]
 */
actual fun provideDeviceInfo(): DeviceInfo {
    val desktopInfo = KInfoState().desktopInfo
    val operatingSystem = desktopInfo.operatingSystem
    val computerInfo = desktopInfo.hardware.computerSystem
    return DeviceInfo(
        uniqueIdentifier = computerInfo.baseboard.serialNumber,
        brand = operatingSystem.manufacturer,
        model = computerInfo.model.whenIsToBeFilledByOEM { CUSTOM_BUILD },
        os = operatingSystem.family,
        osVersion = operatingSystem.versionInfo.version
    )
}