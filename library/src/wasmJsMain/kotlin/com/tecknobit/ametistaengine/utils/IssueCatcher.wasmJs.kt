package com.tecknobit.ametistaengine.utils

import com.tecknobit.ametistaengine.AmetistaEngine
import com.tecknobit.ametistaengine.configuration.EngineConfiguration.Companion.ISSUE_KEY
import kotlinx.browser.window

private const val UNCAUGHT_PROMISE_EXCEPTION_TYPE = "unhandledrejection"

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

private fun storeIssueLocally(
    issue: Throwable
) {
    window.localStorage.setItem(ISSUE_KEY, issue.stackTraceToString())
}