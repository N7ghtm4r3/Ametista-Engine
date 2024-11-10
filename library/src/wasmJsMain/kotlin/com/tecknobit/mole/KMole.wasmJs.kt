package com.tecknobit.mole

import kotlinx.browser.window

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class KMole {

    actual companion object {

        actual val kMole by lazy { KMole() }

        private const val UNKNOWN = "unknown"

        private val brandRegex =
            "(Apple|Samsung|Google|Huawei|LG|Sony|Motorola|Nokia|HTC|OnePlus|Xiaomi|Lenovo|Asus|Oppo|Realme|Vivo|Alcatel|PlayStation|Xbox|iPhone|iPad|iPod|Microsoft|Amazon|Razer|ZTE|Meizu|Sharp|TCL|BlackBerry|Honor|Bose|Nintendo|Roku|Wiko|Fairphone|Nubia|LeEco)".toRegex()

        private val modelRegex = "(iPhone\\s[0-9]+|iPad\\s[0-9]+|SM-[A-Za-z0-9]+|[A-Za-z0-9]+(?:\\s[0-9]+)?)".toRegex()

        private val osRegex =
            "(Android|iOS|Windows|Mac OS X|Linux|BlackBerry|Windows Phone|PlayStation|Xbox|tvOS|Fire OS)".toRegex()

        private val osVersionRegex =
            "(Android|iOS|Windows NT|Mac OS X|Linux|BlackBerry|Windows Phone|PlayStation|Xbox|tvOS)[ /](\\d+[.\\d]+|\\d+[.\\d]+[A-Za-z]*)".toRegex()

        private val browserRegex =
            "(Chrome|Safari|Firefox|Edge|Opera|MSIE|Trident|Chromium|Brave|Vivaldi|UC Browser|SamsungBrowser|Edge Chromium|IE|Safari Mobile)".toRegex()

        private val browserVersionRegex =
            "(?:Chrome|Safari|Firefox|Edge|Opera|MSIE|Trident|Chromium|Brave|Vivaldi|UC Browser|SamsungBrowser|Edge Chromium|IE|Safari Mobile)/(\\d+[.\\d]+)".toRegex()

    }

    actual val uniqueIdentifier: String = window.navigator.userAgent

    actual val brand: String

    actual val model: String

    actual val os: String

    actual val osVersion: String

    val browser: String

    val browserVersion: String

    init {
        brand = findBrand()
        model = findModel()
        os = findOs()
        osVersion = findOsVersion()
        browser = findBrowser()
        browserVersion = findBrowserVersion()
        println("browser_version: $browserVersion")
        println("browser: $browser")
    }

    private fun findBrand(): String {
        return parseUserAgent(
            regex = brandRegex
        )
    }

    private fun findModel(): String {
        return parseUserAgent(
            regex = modelRegex,
            index = 2
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

}