package io.github.sgpublic.exspplugin.core.kt

import io.github.sgpublic.exsp.annotations.ExSharedPreference
import io.github.sgpublic.exspplugin.base.PsiProcess
import io.github.sgpublic.exspplugin.util.hasAnnotation
import io.github.sgpublic.exspplugin.util.ktParent
import io.github.sgpublic.exspplugin.util.log
import org.jetbrains.kotlin.idea.core.getOrCreateCompanionObject
import org.jetbrains.kotlin.nj2k.postProcessing.type
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.containingClass

class KtPsiPropertyProcess(clazz: KtObjectDeclaration): PsiProcess<KtObjectDeclaration, KtProperty>(clazz) {
    override fun process(): Collection<KtProperty> {
        if (!OriginElement.ktParent.hasAnnotation(ExSharedPreference::class.java)) {
            return emptyList()
        }

        log.info("Process kotlin property: $Name")
        val result = mutableListOf<KtProperty>()

        val factory = KtPsiFactory(Project, true)
        for (property in OriginElement.ktParent.getProperties()) {
            val ktProperty = factory.createProperty(property.name!!, property.type()?.toString(), true)
            result.add(ktProperty)
        }

        return result
    }

    override val Name: String = clazz.name ?: ""
}