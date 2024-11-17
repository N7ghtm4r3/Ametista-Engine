@file:OptIn(ExperimentalForeignApi::class)

package com.tecknobit.ametistaengine.utils

import com.tecknobit.ametistaengine.AmetistaEngine
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.staticCFunction
import platform.Foundation.NSException
import platform.Foundation.NSSetUncaughtExceptionHandler

/**
 * Method to catch an issue occurred during the runtime of the application.
 *
 * This will automatically catch the unhandled global exception and send the related report to the collector server
 */
actual fun catchIssue() {
    NSSetUncaughtExceptionHandler(staticCFunction(::exceptionHandler))
}

/**
 * Method to send the related report to the collector server
 *
 * @param exception The issue occurred
 */
private fun exceptionHandler(
    exception: NSException?
) {
    val ametistaEngine = AmetistaEngine.ametistaEngine
    val issue = Exception(exception?.reason)
    ametistaEngine.notifyIssue(issue)
}