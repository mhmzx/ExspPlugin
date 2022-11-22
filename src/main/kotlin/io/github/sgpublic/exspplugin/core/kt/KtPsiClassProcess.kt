package io.github.sgpublic.exspplugin.core.kt

import io.github.sgpublic.exsp.annotations.ExSharedPreference
import io.github.sgpublic.exspplugin.base.PsiProcess
import io.github.sgpublic.exspplugin.util.*
import org.jetbrains.kotlin.nj2k.postProcessing.type
import org.jetbrains.kotlin.psi.*

class KtPsiClassProcess(clazz: KtObjectDeclaration): PsiProcess<KtObjectDeclaration, KtClass>(clazz) {
    override fun process(): Collection<KtClass> {
        if (!OriginElement.ktParent.hasAnnotation(ExSharedPreference::class.java)) {
            return emptyList()
        }

        log.info("Process kotlin editor class: $Name")

        val Editor = OriginElement.createEditorClass()

        val factory = KtPsiFactory(Editor.project)

        for (property in OriginElement.ktParent.getProperties()) {
            val function = factory.createFunction("fun ${property.SetterName}(value: ${property.type()}): ${Editor.type()}")
            Editor.addDeclaration(function)
        }

        return listOf(Editor)
    }

    override val Name: String = OriginElement.name ?: ""
}