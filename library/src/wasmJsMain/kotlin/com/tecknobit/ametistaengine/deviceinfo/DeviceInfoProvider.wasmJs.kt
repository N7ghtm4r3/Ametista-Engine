package com.tecknobit.ametistaengine.deviceinfo

import kotlinx.browser.window

private const val UNKNOWN = "unknown"

private val brandRegex =
    """\b(Apple|Samsung|Google|Huawei|LG|Sony|Motorola|Nokia|HTC|OnePlus|Xiaomi|Lenovo|Asus|Oppo|Realme|Vivo|Alcatel|PlayStation|Xbox|iPhone|iPad|iPod|Microsoft|Amazon|Razer|ZTE|Meizu|Sharp|TCL|BlackBerry|Honor|Bose|Nintendo|Roku|Wiko|Fairphone|Nubia|LeEco)\b""".toRegex()

private val modelRegex =
    """\b(iPhone\s[0-9A-Za-z]+|iPad\s[0-9A-Za-z]+|SM-[A-Za-z0-9]+|Galaxy\s[A-Za-z0-9\s]+|Pixel\s[0-9A-Za-z]+|Nexus\s[0-9]+|OnePlus\s[A-Za-z0-9]+|Xperia\s[0-9A-Za-z]+|Redmi\s[0-9A-Za-z]+|Mi\s[0-9A-Za-z]+|Poco\s[0-9A-Za-z]+|Honor\s[0-9A-Za-z]+|Mate\s[0-9A-Za-z]+|P[0-9]+|Nova\s[0-9]+|Y[0-9]+|Lenovo\s[A-Za-z0-9]+|ThinkPad\s[0-9A-Za-z]+|Surface\s[0-9A-Za-z]+|HTC\s[A-Za-z0-9]+|LG\s[A-Za-z0-9]+|Oppo\s[A-Za-z0-9]+|Realme\s[A-Za-z0-9]+|Vivo\s[A-Za-z0-9]+|ZTE\s[A-Za-z0-9]+|Alcatel\s[A-Za-z0-9]+|TCL\s[A-Za-z0-9]+|BlackBerry\s[A-Za-z0-9]+|Nintendo\sSwitch|PlayStation\s[0-9]+|Xbox\s(One|Series\sX|Series\sS)|Razer\sPhone\s[0-9]*)\b""".toRegex(
        RegexOption.IGNORE_CASE
    )

private val osRegex =
    """\b(Android|iOS|Windows(?: NT)?|Mac OS X|Linux|BlackBerry|Windows Phone|PlayStation|Xbox|tvOS|Fire OS)\b""".toRegex()

private val osVersionRegex =
    """\b(Android|iOS|Windows NT|Mac OS X|Linux|BlackBerry|Windows Phone|PlayStation|Xbox|tvOS|Fire OS)[ /](\d+(?:\.\d+)*[A-Za-z]*)\b""".toRegex()

private val browserRegex =
    """\b(Chrome|Safari|Firefox|Edge|Opera|MSIE|Trident|Chromium|Brave|Vivaldi|UC Browser|SamsungBrowser|Edge Chromium|IE|Safari Mobile)\b""".toRegex()

private val browserVersionRegex =
    """\b(?:Chrome|Safari|Firefox|Edge|Opera|MSIE|Trident|Chromium|Brave|Vivaldi|UC Browser|SamsungBrowser|Edge Chromium|IE|Safari Mobile)[/ ](\d+(?:\.\d+)*)\b""".toRegex()

private var uniqueIdentifier: String = ""

actual fun provideDeviceInfo(): DeviceInfo {
    uniqueIdentifier = window.navigator.userAgent
    val brand = findBrand()
    val model = findModel()
    val os = findOs()
    val osVersion = findOsVersion()
    val browser = findBrowser()
    val browserVersion = findBrowserVersion()
    return WebDeviceInfo(
        uniqueIdentifier = uniqueIdentifier,
        brand = brand,
        model = model,
        os = os,
        osVersion = osVersion,
        browser = browser,
        browserVersion = browserVersion
    )
}

private fun findBrand(): String {
    return parseUserAgent(
        regex = brandRegex
    )
}

private fun findModel(): String {
    return parseUserAgent(
        regex = modelRegex
    )
}

private fun findOs(): String {
    return parseUserAgent(
        regex = osRegex
    )
}

private fun findOsVersion(): String {
    return parseUserAgent(
        regex = osVersionRegex
    )
}

private fun findBrowser(): String {
    return parseUserAgent(
        regex = browserRegex
    )
}

private fun findBrowserVersion(): String {
    return parseUserAgent(
        regex = browserVersionRegex
    )
}

private fun parseUserAgent(
    regex: Regex,
    index: Int = 1
): String {
    val find = regex.find(uniqueIdentifier)?.groups?.get(index)
    return if (find == null)
        UNKNOWN
    else
        find.value
}