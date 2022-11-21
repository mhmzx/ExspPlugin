package io.github.sgpublic.exspplugin.core

import com.intellij.psi.*
import com.intellij.psi.augment.PsiAugmentProvider
import io.github.sgpublic.exspplugin.base.PsiProcess

class PsiAugmentProvider: PsiAugmentProvider() {
    override fun <Psi : PsiElement> getAugments(
        element: PsiElement, type: Class<Psi>, nameHint: String?
    ): MutableList<Psi> {
        val result = mutableListOf<Psi>()
        val methods = PsiProcess.of(element, type)
            ?.process() ?: return result
        result.addAll(methods)
        return result
    }
}