package com.tecknobit.ametistaengine.utils

actual fun catchIssue() {
    // TODO: TO TEST
    NSSetUncaughtExceptionHandler { exception ->
        println(exception)
    }
}