package io.github.sgpublic.exspplugin.base

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import io.github.sgpublic.exspplugin.core.java.JavaPsiClassProcess
import io.github.sgpublic.exspplugin.core.java.JavaPsiMethodProcess
import io.github.sgpublic.exspplugin.core.kt.KtPsiClassProcess
import io.github.sgpublic.exspplugin.core.kt.KtPsiComObjProcess
import io.github.sgpublic.exspplugin.core.kt.KtPsiMethodProcess
import io.github.sgpublic.exspplugin.core.kt.KtPsiPropertyProcess
import io.github.sgpublic.exspplugin.util.ktParent
import io.github.sgpublic.exspplugin.util.log
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
                is KtClass -> {
                    log.info("Current element is KtClass, require $type: ${element.name}")
                    when (type) {
                        KtObjectDeclaration::class.java -> KtPsiComObjProcess(element)
                        else -> null
                    }
                }
                is KtObjectDeclaration -> {
                    log.info("Current element is KtObjectDeclaration, require $type: ${element.name}")
                    if (!element.isCompanion()) {
                        return null
                    }
                    when (type) {
                        KtFunction::class.java -> KtPsiMethodProcess(element)
                        KtClass::class.java -> KtPsiClassProcess(element)
                        KtProperty::class.java -> KtPsiPropertyProcess(element)
                        else -> {
                            log.info("Current element is KtObjectDeclaration and isCompanion, but require $type: ${element.ktParent.name}")
                            null
                        }
                    }
                }
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