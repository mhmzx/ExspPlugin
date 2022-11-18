package io.github.sgpublic.exspplugin.impl

import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.*
import com.intellij.psi.augment.PsiAugmentProvider
import com.intellij.psi.impl.light.LightMethodBuilder
import com.intellij.psi.impl.light.LightPsiClassBuilder
import io.github.sgpublic.exsp.annotations.ExSharedPreference
import io.github.sgpublic.exsp.annotations.ExValue
import io.github.sgpublic.exspplugin.util.log
import org.jetbrains.kotlin.asJava.LightClassBuilder
import org.jetbrains.kotlin.lombok.utils.capitalize
import org.jetbrains.kotlin.resolve.jvm.KotlinJavaPsiFacade
import java.util.*

class PsiAugmentProvider: PsiAugmentProvider() {
    override fun <Psi : PsiElement> getAugments(
        element: PsiElement, type: Class<Psi>, nameHint: String?
    ): MutableList<Psi> {
        if (element is PsiClass) {
            log.debug("Process class: ${element.name}")
            if (type.isAssignableFrom(PsiMethod::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return process(element) as MutableList<Psi>
            }
        }
        log.warn("Skip process!")
        return Collections.emptyList()
    }

    private fun process(element: PsiClass): MutableList<PsiMethod> {
        val result = mutableListOf<PsiMethod>()

        if (!element.hasAnnotation(ExSharedPreference::class.java.packageName)) {
            log.debug("This class dose not add @ExSharedPreference, skip!")
            return result
        }

        val type = KotlinJavaPsiFacade.getInstance(element.project)

        LightMethodBuilder(element.manager, JavaLanguage.INSTANCE, "edit")
            .addModifiers(PsiModifier.PUBLIC, PsiModifier.STATIC)
            .setMethodReturnType(type)
            .setContainingClass(element)
            .also {
                it.navigationElement = element
            }
            .let {
                result.add(it)
            }

        for (field in element.fields) {
            log.debug("package name: ${ExValue::class.java.packageName}")
            if (!field.hasAnnotation(ExValue::class.java.packageName) ||
                field.hasModifierProperty(PsiModifier.FINAL) ||
                element.hasAnnotation("lombok.Data")) {
                continue
            }

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
        }

        return result
    }
}