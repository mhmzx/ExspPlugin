package io.github.sgpublic.xxprefplugin.core

import com.intellij.psi.*
import com.intellij.psi.augment.PsiAugmentProvider
import io.github.sgpublic.xxprefplugin.base.PsiProcess

class PsiAugmentProvider: PsiAugmentProvider() {
    override fun <Psi : PsiElement> getAugments(
        element: PsiElement, type: Class<Psi>, nameHint: String?
    ): MutableList<Psi> {
        val result = mutableListOf<Psi>()
        result.addAll(PsiProcess.of(element, type)
            ?.process() ?: return result)
        return result
    }
}