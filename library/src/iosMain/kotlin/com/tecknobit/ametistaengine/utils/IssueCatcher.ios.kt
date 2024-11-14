package com.tecknobit.ametistaengine.utils

import com.tecknobit.ametistaengine.AmetistaEngine

actual fun catchIssue() {
    NSSetUncaughtExceptionHandler { exception ->
        val ametistaEngine = AmetistaEngine.ametistaEngine
        val issue = Exception(exception.reason)
        ametistaEngine.notifyIssue(
            issue = issue
        )
    }
}