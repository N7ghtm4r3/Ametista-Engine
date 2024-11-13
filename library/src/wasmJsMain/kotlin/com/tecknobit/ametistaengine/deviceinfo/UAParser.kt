package com.tecknobit.ametistaengine.deviceinfo

@JsModule("ua-parser-js")
external class UAParser(
    userAgent: String
) {

    fun getResult(): UAParserResult

}

external interface UAParserResult {
    val device: Device
    val browser: Browser
    val os: Os
}

external interface Device {
    val model: String?
    val type: String?
    val vendor: String?
}

external interface Browser {
    val name: String?
    val version: String?
}

external interface Os {
    val name: String?
    val version: String?
}

fun parseUserAgent(
    userAgent: String
): UAParserResult {
    return UAParser(userAgent).getResult()
}