package io.github.sgpublic.xxprefplugin.core.java

import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiModifier
import io.github.sgpublic.xxpref.annotations.XXPreference
import io.github.sgpublic.xxprefplugin.base.PsiProcess
import io.github.sgpublic.xxprefplugin.base.java.PsiMethodBuilder
import io.github.sgpublic.xxprefplugin.util.SetterName
import io.github.sgpublic.xxprefplugin.util.createEditorClass
import io.github.sgpublic.xxprefplugin.util.getType

open class JavaPsiClassProcess(clazz: PsiClass): PsiProcess<PsiClass, PsiClass>(clazz) {
    override fun process(): Collection<PsiClass> {
        val result = mutableListOf<PsiClass>()

        OriginElement.getAnnotation(XXPreference::class.java.canonicalName) ?: return result

        val Editor = OriginElement.createEditorClass()

        for (field in OriginElement.fields) {
            PsiMethodBuilder(Editor.manager, JavaLanguage.INSTANCE, field.SetterName)
                .addModifiers(PsiModifier.PUBLIC)
                .setMethodReturnType(Editor.getType())
                .addParameter("value", field.type)
                .setContainingClass(field.containingClass)
                .also {
                    it.navigationElement = field
                }
                .let {
                    Editor.addMethod(it)
                }
        }

        result.add(Editor)

        return result
    }

    override val Name: String = OriginElement.name ?: ""
}