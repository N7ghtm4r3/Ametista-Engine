package com.tecknobit.mole

import oshi.SystemInfo

actual class KMole private actual constructor() {

    actual companion object {

        actual val kMole: KMole by lazy { KMole() }

    }

    private val info = SystemInfo()

    private val operatingSystem = info.operatingSystem

    private val computerInfo = info.hardware.computerSystem

    actual val uniqueIdentifier: String
        get() = computerInfo.baseboard.serialNumber

    actual val brand: String
        get() = operatingSystem.manufacturer

    actual val model: String
        get() {
            val model = computerInfo.model
            return if (model != "To Be Filled By O.E.M.")
                model
            else
                computerInfo.baseboard.model
        }

    actual val os: String
        get() = operatingSystem.family

    actual val osVersion: String
        get() = operatingSystem.versionInfo.version

}