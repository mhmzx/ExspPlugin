package io.github.sgpublic.exspplugin.core.kt

import com.intellij.psi.PsiClass
import io.github.sgpublic.exspplugin.base.PsiProcess
import org.jetbrains.kotlin.psi.KtClass
import java.util.*

class KtPsiClassProcess(clazz: KtClass): PsiProcess<KtClass, KtClass>(clazz) {
    override fun process(): Collection<KtClass> {
        val result = mutableListOf<KtClass>()

        return result
    }

    override val Name: String = OriginElement.name ?: ""
}