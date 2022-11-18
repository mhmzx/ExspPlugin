package io.github.sgpublic.exspplugin.util

import com.intellij.openapi.diagnostic.Logger

val Any.log: Logger get() {
    return Logger.getInstance(javaClass)
}