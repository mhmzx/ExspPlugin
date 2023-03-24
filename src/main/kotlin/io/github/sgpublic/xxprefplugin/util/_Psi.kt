package io.github.sgpublic.xxprefplugin.util

import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.*
import com.intellij.psi.impl.light.LightPsiClassBuilder
import io.github.sgpublic.xxprefplugin.base.JavaEditorClassBuilder
import io.github.sgpublic.xxprefplugin.base.PsiMethodBuilder

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
        .addExtends("io.github.sgpublic.xxpref.PrefEditor")
        .setContainingClass(this)
}

fun PsiClass.getEditorClass(): PsiClass {
    return findInnerClassByName(JavaEditorClassBuilder.Name, false)!!
}