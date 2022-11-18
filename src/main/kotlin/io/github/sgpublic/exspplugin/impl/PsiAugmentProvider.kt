package io.github.sgpublic.exspplugin.impl

import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.*
import com.intellij.psi.augment.PsiAugmentProvider
import com.intellij.psi.impl.light.LightMethodBuilder
import io.github.sgpublic.exsp.annotations.ExSharedPreference
import io.github.sgpublic.exsp.annotations.ExValue
import io.github.sgpublic.exspplugin.util.log
import org.jetbrains.kotlin.lombok.utils.capitalize
import java.util.*

class PsiAugmentProvider: PsiAugmentProvider() {
    override fun <Psi : PsiElement?> getAugments(
        element: PsiElement, type: Class<Psi>, nameHint: String?
    ): MutableList<Psi> {
        if (element is PsiClass && type.isAssignableFrom(PsiMethod::class.java)) {
            log.debug("Process class: ${element.name}")
            @Suppress("UNCHECKED_CAST")
            return process(element) as MutableList<Psi>
        }
        log.debug("Skip process!")
        return Collections.emptyList()
    }

    private fun process(element: PsiClass): MutableList<PsiMethod> {
        val result = mutableListOf<PsiMethod>()

        if (!element.hasAnnotation(ExSharedPreference::class.java.packageName)) {
            log.debug("This class dose not add @ExSharedPreference, skip!")
            return result
        }

        for (field in element.fields) {
            log.debug("package name: ${ExValue::class.java.packageName}")
            if (!field.hasAnnotation(ExValue::class.java.packageName) ||
                field.hasModifierProperty(PsiModifier.FINAL)) {
                continue
            }

            if (!element.hasAnnotation("lombok.Data")) {
                result.addAll(addGeSetter(field))
            }
        }


        return result
    }

    private fun addGeSetter(field: PsiField): List<PsiMethod> {
        val result = mutableListOf<PsiMethod>()

        LightMethodBuilder(field.manager, JavaLanguage.INSTANCE, "get${field.name.capitalize()}")
            .addModifiers(PsiModifier.PUBLIC, PsiModifier.STATIC)
            .setMethodReturnType(field.type)
            .setContainingClass(field.containingClass)
            .also {
                it.navigationElement = field
            }
            .let {
                result.add(it)
            }

        LightMethodBuilder(field.manager, JavaLanguage.INSTANCE, "set${field.name.capitalize()}")
            .addModifiers(PsiModifier.PUBLIC, PsiModifier.STATIC)
            .setMethodReturnType(PsiType.VOID)
            .setContainingClass(field.containingClass)
            .also {
                it.navigationElement = field
            }
            .let {
                result.add(it)
            }

        return result
    }
}