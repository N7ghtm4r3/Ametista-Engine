package com.tecknobit.ametistaengine.utils

import com.tecknobit.ametistaengine.AmetistaEngine

/**
 * Method to catch an issue occurred during the runtime of the application.
 *
 * This will automatically catch the unhandled global exception and send the related report to the collector server
 */
actual fun catchIssue() {
    NSSetUncaughtExceptionHandler { exception ->
        val ametistaEngine = AmetistaEngine.ametistaEngine
        val issue = Exception(exception.reason)
        ametistaEngine.notifyIssue(
            issue = issue
        )
    }
}