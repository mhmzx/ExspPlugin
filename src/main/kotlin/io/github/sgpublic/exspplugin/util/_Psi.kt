package io.github.sgpublic.exspplugin.util

import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.*
import com.intellij.psi.impl.light.LightPsiClassBuilder
import io.github.sgpublic.exspplugin.base.JavaEditorClassBuilder
import io.github.sgpublic.exspplugin.base.PsiMethodBuilder
import org.jetbrains.kotlin.idea.base.utils.fqname.getKotlinFqName
import org.jetbrains.kotlin.lombok.utils.capitalize
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtPsiFactory

fun LightPsiClassBuilder.addModifier(vararg modifiers: String): LightPsiClassBuilder {
    val list = modifierList
    for (modifier in modifiers) {
        list.addModifier(modifier)
    }
    return this
}

fun PsiClass.getType(): PsiType {
    return JavaPsiFacade.getElementFactory(project).createType(this)
}

val PsiField.IsBoolean: Boolean get() = type.equalsToText("boolean") || type.equalsToText("java.lang.Boolean")

val PsiField.GetterName: String get() {
    val name = name.capitalize()
    return if (IsBoolean) {
        "is$name"
    } else {
        "get$name"
    }
}

val PsiField.SetterName: String get() {
    val name = name.capitalize()
    return "set$name"
}

fun PsiClass.createEditorClass(): LightPsiClassBuilder {
    val constructor = PsiMethodBuilder(manager, JavaLanguage.INSTANCE, name ?: "")
        .addModifiers(PsiModifier.PRIVATE)
        .setConstructor(true)
        .addParameter("editor", "android.content.SharedPreferences.Editor")
    return JavaEditorClassBuilder(this)
        .addModifier(PsiModifier.PUBLIC, PsiModifier.STATIC)
        .addConstructor(constructor)
        .addExtends("io.github.sgpublic.exsp.SpEditor")
        .setContainingClass(this)
}

fun PsiClass.getEditorClass(): PsiClass {
    return findInnerClassByName("Editor", false)!!
}