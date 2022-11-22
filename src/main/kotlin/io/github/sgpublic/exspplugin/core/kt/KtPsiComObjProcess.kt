package io.github.sgpublic.exspplugin.core.kt

import io.github.sgpublic.exsp.annotations.ExSharedPreference
import io.github.sgpublic.exspplugin.base.PsiProcess
import io.github.sgpublic.exspplugin.util.hasAnnotation
import io.github.sgpublic.exspplugin.util.ktParent
import io.github.sgpublic.exspplugin.util.log
import org.jetbrains.kotlin.idea.core.getOrCreateCompanionObject
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.containingClass

class KtPsiComObjProcess(clazz: KtClass): PsiProcess<KtClass, KtObjectDeclaration>(clazz) {
    override fun process(): Collection<KtObjectDeclaration> {
        if (!OriginElement.hasAnnotation(ExSharedPreference::class.java)) {
            return emptyList()
        }

        log.info("Process kotlin companion object: $Name")
        val comObj = OriginElement.getOrCreateCompanionObject()
        val factory = KtPsiFactory(Project, true)
        comObj.addSuperTypeListEntry(factory.createSuperTypeEntry(Name))
        return mutableListOf(comObj)
    }

    override val Name: String = clazz.name ?: ""
}