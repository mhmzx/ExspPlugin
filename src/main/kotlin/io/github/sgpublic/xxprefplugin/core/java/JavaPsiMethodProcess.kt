package io.github.sgpublic.xxprefplugin.core.java

import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.*
import io.github.sgpublic.xxpref.annotations.PrefVal
import io.github.sgpublic.xxpref.annotations.XXPreference
import io.github.sgpublic.xxprefplugin.base.PsiMethodBuilder
import io.github.sgpublic.xxprefplugin.base.PsiProcess
import io.github.sgpublic.xxprefplugin.util.*

open class JavaPsiMethodProcess(clazz: PsiClass): PsiProcess<PsiClass, PsiMethod>(clazz) {
    override fun process(): Collection<PsiMethod> {
        val result = mutableListOf<PsiMethod>()

        val annotation = OriginElement.getAnnotation(XXPreference::class.java.canonicalName) ?: return result

        if (OriginElement.hasAnnotation("lombok.Data")) {
            log.warn("DO NOT add @lombok.Data with @ExSharedPreference together! In class: $Name")
            return result
        }

        val Editor = OriginElement.getEditorClass()

        PsiMethodBuilder(OriginElement.manager, JavaLanguage.INSTANCE, "edit")
            .addModifiers(PsiModifier.PUBLIC, PsiModifier.STATIC)
            .setMethodReturnType(Editor.getType())
            .setContainingClass(OriginElement)
            .also {
                it.navigationElement = annotation
            }
            .let {
                result.add(it)
            }

        for (field in OriginElement.fields) {
            if (!field.hasAnnotation(PrefVal::class.java.canonicalName) ||
                field.hasModifierProperty(PsiModifier.FINAL)) {
                continue
            }

            if (field.hasAnnotation("lombok.Data")) {
                log.warn("DO NOT add @lombok.Data with @ExSharedPreference together! In class: $Name")
                continue
            }

            PsiMethodBuilder(OriginElement.manager, JavaLanguage.INSTANCE, field.GetterName)
                .addModifiers(PsiModifier.PUBLIC, PsiModifier.STATIC)
                .setMethodReturnType(field.type)
                .setContainingClass(field.containingClass)
                .also {
                    it.navigationElement = field
                }
                .let {
                    result.add(it)
                }


            val liveData = JavaPsiFacade.getElementFactory(Project)
                .createTypeFromText("androidx.lifecycle.LiveData<${field.type.canonicalText}>", OriginElement)

            PsiMethodBuilder(OriginElement.manager, JavaLanguage.INSTANCE, "${field.GetterName}Observer")
                .addModifiers(PsiModifier.PUBLIC, PsiModifier.STATIC)
                .setMethodReturnType(liveData)
                .setContainingClass(field.containingClass)
                .also {
                    it.navigationElement = field
                }
                .let {
                    result.add(it)
                }

            PsiMethodBuilder(OriginElement.manager, JavaLanguage.INSTANCE, field.SetterName)
                .addModifiers(PsiModifier.PUBLIC, PsiModifier.STATIC)
                .setMethodReturnType(PsiType.VOID)
                .addParameter("value", field.type)
                .setContainingClass(field.containingClass)
                .also {
                    it.navigationElement = field
                }
                .let {
                    result.add(it)
                }
        }

        return result
    }

    override val Name: String = OriginElement.name ?: ""
}