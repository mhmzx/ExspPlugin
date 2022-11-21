package io.github.sgpublic.exspplugin.base

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import io.github.sgpublic.exspplugin.core.java.JavaPsiClassProcess
import io.github.sgpublic.exspplugin.core.java.JavaPsiMethodProcess
import io.github.sgpublic.exspplugin.core.kt.KtPsiClassProcess
import io.github.sgpublic.exspplugin.core.kt.KtPsiMethodProcess
import org.jetbrains.kotlin.asJava.elements.KtLightMethod
import org.jetbrains.kotlin.psi.KtClass

abstract class PsiProcess<T1: PsiElement, T2: PsiElement>(
    protected val OriginElement: T1,
) {
    abstract fun process(): Collection<T2>
    protected val Project = OriginElement.project
    protected abstract val Name: String

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <T1: PsiElement, T2: PsiElement> of(element: T1, type: Class<T2>): PsiProcess<T1, T2>? {
            return when (element) {
                is PsiClass -> {
                    when (type) {
                        PsiMethod::class.java -> JavaPsiMethodProcess(element)
                        PsiClass::class.java -> JavaPsiClassProcess(element)
                        else -> null
                    }
                }
                is KtClass -> {
                    when (type) {
                        KtLightMethod::class.java -> KtPsiMethodProcess(element)
                        KtClass::class.java -> KtPsiClassProcess(element)
                        else -> null
                    }
                }
                else -> null
            } as PsiProcess<T1, T2>?
        }
    }
}