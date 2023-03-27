package io.github.sgpublic.xxprefplugin.core.java

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.PsiModifier
import io.github.sgpublic.xxprefplugin.base.PsiProcess
import io.github.sgpublic.xxprefplugin.base.java.PsiFieldBuilder
import io.github.sgpublic.xxprefplugin.util.getType

open class JavaPsiFieldProcess(clazz: PsiClass): PsiProcess<PsiClass, PsiField>(clazz) {
    override fun process(): Collection<PsiField> {
        val result = mutableListOf<PsiField>()

        PsiFieldBuilder(OriginElement.manager, "INSTANCE", OriginElement.getType())
            .addModifiers(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
            .setContainingClass(OriginElement)
            .also {
                it.navigationElement = OriginElement
            }
            .let {
                result.add(it)
            }

        return result
    }

    override val Name: String = OriginElement.name ?: ""
}