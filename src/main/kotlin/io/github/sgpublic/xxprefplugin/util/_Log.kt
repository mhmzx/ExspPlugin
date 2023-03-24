package io.github.sgpublic.xxprefplugin.util

import com.intellij.openapi.diagnostic.LoggerRt

val Any.log: LoggerRt get() {
    return LoggerRt.getInstance(javaClass)
}