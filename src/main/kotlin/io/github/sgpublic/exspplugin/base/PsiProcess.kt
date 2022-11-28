package io.github.sgpublic.exspplugin.base

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import io.github.sgpublic.exspplugin.core.java.JavaPsiClassProcess
import io.github.sgpublic.exspplugin.core.java.JavaPsiMethodProcess
import org.jetbrains.kotlin.psi.*

abstract class PsiProcess<T1: PsiElement, T2: PsiElement>(
    protected val OriginElement: T1,
) {
    abstract fun process(): Collection<T2>
    protected val Project = OriginElement.project
    protected abstract val Name: String
    protected val Parent = OriginElement.parent

    companion object {
        fun <T1: PsiElement, T2: PsiElement> of(element: T1, type: Class<T2>): PsiProcess<T1, T2>? {
            val result = when (element) {
                is PsiClass -> {
                    when (type) {
                        PsiMethod::class.java -> JavaPsiMethodProcess(element)
                        PsiClass::class.java -> JavaPsiClassProcess(element)
                        else -> null
                    }
                }
                else -> null
            }
            @Suppress("UNCHECKED_CAST")
            return result as PsiProcess<T1, T2>?
        }
    }
}