package com.tecknobit.ametistaengine.utils

import com.tecknobit.ametistaengine.AmetistaEngine
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.ISSUE_KEY
import kotlinx.browser.window

/**
 * **UNCAUGHT_PROMISE_EXCEPTION_TYPE** -> constant "unhandledrejection" value
 */
private const val UNCAUGHT_PROMISE_EXCEPTION_TYPE = "unhandledrejection"

/**
 * Method to catch an issue occurred during the runtime of the application.
 *
 * This will automatically catch the unhandled global exception and send the related report to the collector server
 */
actual fun catchIssue() {
    sendIssueLocallyStored()
    window.addEventListener(UNCAUGHT_PROMISE_EXCEPTION_TYPE) { event ->
        event.toThrowableOrNull()?.let { issue ->
            storeIssueLocally(
                issue = issue
            )
        }
    }
    window.onerror = { _, _, _, _, error ->
        error?.toThrowableOrNull()?.let { issue ->
            storeIssueLocally(
                issue = issue
            )
        }
        true.toJsBoolean()
    }
}

/**
 * Method to send locally storage issue previously saved
 */
private fun sendIssueLocallyStored() {
    val ametistaEngine = AmetistaEngine.ametistaEngine
    ametistaEngine.execAfterConfigurationLoaded {
        val localStorage = window.localStorage
        val issue = localStorage.getItem(ISSUE_KEY)
        issue?.let {
            ametistaEngine.notifyIssue(
                issue = issue
            )
            localStorage.removeItem(ISSUE_KEY)
        }
    }
}

/**
 * Method to store an issue occurred locally and then sent to the collector server
 *
 * @param issue The issue to store
 */
private fun storeIssueLocally(
    issue: Throwable
) {
    window.localStorage.setItem(ISSUE_KEY, issue.stackTraceToString())
}