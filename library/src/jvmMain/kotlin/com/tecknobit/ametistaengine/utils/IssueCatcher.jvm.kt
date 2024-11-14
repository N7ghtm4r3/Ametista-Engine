package com.tecknobit.ametistaengine.utils

import com.tecknobit.ametistaengine.AmetistaEngine

actual fun catchIssue() {
    val defHandler = Thread.getDefaultUncaughtExceptionHandler()
    Thread.setDefaultUncaughtExceptionHandler { t, e ->
        val ametistaEngine = AmetistaEngine.ametistaEngine
        ametistaEngine.notifyIssue(
            issue = e
        )
        defHandler?.uncaughtException(t, e)
    }
}