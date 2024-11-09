package com.tecknobit.mole

import kotlinx.browser.window

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class KMole {

    actual companion object {

        actual val kMole by lazy { KMole() }

        private const val VERSION_REGEX: String = "\\b(v?)(\\d+(\\.\\d+)*)([-_]?([a-zA-Z]+[-_]?\\d*)?)?\\b"

        private const val BROWSER_REGEX: String =
            "^(Google Chrome|Safari|Microsoft Edge|Mozilla Firefox|Opera|Samsung Internet|Brave|Vivaldi|Tor Browser|Epic Privacy Browser|Maxthon)$"

    }

    /**
     * package com.tecknobit.mole
     *
     * import kotlinx.browser.window
     *
     * @Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
     * actual class KMole {
     *
     *     actual companion object {
     *
     *         actual val kMole by lazy { KMole() }
     *
     *     }
     *
     *     actual val uniqueIdentifier: String = window.
     *
     *     actual val brand: String
     *         get() = TODO("Not yet implemented")
     *
     *     actual val model: String
     *         get() = TODO("Not yet implemented")
     *
     *     actual val os: String
     *         get() = TODO("Not yet implemented")
     *
     *     actual val osVersion: String
     *         get() = TODO("Not yet implemented")
     *
     * }
     */

    // id: Mozilla/5.0 (Linux; Android 13; SM-G981B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36

    actual val uniqueIdentifier: String = window.navigator.userAgent

    actual val brand: String = window.navigator.vendor

    actual val model: String = window.navigator.appName

    actual val os: String
        get() = "TODO(Not yet implemented)"

    actual val osVersion: String
        get() = "TODO(Not yet implemented)"

}