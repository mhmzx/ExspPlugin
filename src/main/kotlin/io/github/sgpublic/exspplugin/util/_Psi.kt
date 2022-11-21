package io.github.sgpublic.exspplugin.util

import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.*
import com.intellij.psi.impl.light.LightPsiClassBuilder
import io.github.sgpublic.exspplugin.base.EditorClassBuilder
import io.github.sgpublic.exspplugin.base.PsiClassBuilder
import io.github.sgpublic.exspplugin.base.PsiMethodBuilder
import org.jetbrains.kotlin.lombok.utils.capitalize

fun LightPsiClassBuilder.addModifier(vararg modifiers: String): LightPsiClassBuilder {
    val list = modifierList
    for (modifier in modifiers) {
        list.addModifier(modifier)
    }
    return this
}

fun PsiClass.getType(): PsiType {
    return JavaPsiFacade.getElementFactory(project).createTypeByFQClassName(qualifiedName ?: "")
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
//    return if (IsBoolean) {
//        "set$name"
//    } else {
//        "set$name"
//    }
}

fun PsiClass.createEditorClass(): LightPsiClassBuilder {
    val constructor = PsiMethodBuilder(manager, JavaLanguage.INSTANCE, name ?: "")
        .setConstructor(true)
        .addParameter("editor", "android.content.SharedPreferences.Editor")
    return EditorClassBuilder(this)
        .addConstructor(constructor)
        .addModifier(PsiModifier.PUBLIC, PsiModifier.STATIC)
        .setContainingClass(this)
}

fun PsiClass.getEditorClass(): PsiClass {
    return findInnerClassByName("Editor", false)!!
}