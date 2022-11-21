package io.github.sgpublic.exspplugin.core.kt

import io.github.sgpublic.exspplugin.base.PsiProcess
import io.github.sgpublic.exspplugin.util.log
import org.jetbrains.kotlin.asJava.elements.KtLightMethod
import org.jetbrains.kotlin.psi.KtClass

class KtPsiMethodProcess(clazz: KtClass): PsiProcess<KtClass, KtLightMethod>(clazz) {
    override fun process(): Collection<KtLightMethod> {
        log.info("Process kotlin class: $Name")
        return emptyList()
    }

    override val Name: String = OriginElement.name ?: ""
}