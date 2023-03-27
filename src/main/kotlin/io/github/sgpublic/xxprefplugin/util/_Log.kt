package io.github.sgpublic.xxprefplugin.util

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.diagnostic.LogLevel
import com.intellij.openapi.diagnostic.Logger

val Any.log: Logger get() {
    return Logger.getInstance(javaClass).also {
        val test = true
        it.setLevel(LogLevel.ALL.takeIf { test } ?: LogLevel.WARNING)
    }
}