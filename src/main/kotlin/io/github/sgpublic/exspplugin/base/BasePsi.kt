package io.github.sgpublic.exspplugin.base

import com.intellij.psi.PsiElement

interface BasePsi<Psi> {
    fun <T: PsiElement?> getAugment(element: Psi): MutableList<T>
}