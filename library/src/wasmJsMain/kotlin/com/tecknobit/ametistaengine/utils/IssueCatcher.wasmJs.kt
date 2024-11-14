package com.tecknobit.ametistaengine.utils

import com.tecknobit.ametistaengine.AmetistaEngine
import kotlinx.browser.window

actual fun catchIssue() {
    window.addEventListener("unhandledrejection") { event ->
        val issue = event.toJsReference().toThrowableOrNull()
        issue?.let {
            val ametistaEngine = AmetistaEngine.ametistaEngine
            ametistaEngine.notifyIssue(
                issue = issue
            )
        }
    }
}