package io.github.sgpublic.xxprefplugin.util

import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.*
import com.intellij.psi.impl.light.LightPsiClassBuilder
import io.github.sgpublic.xxprefplugin.base.java.JavaPsiClassBuilder
import io.github.sgpublic.xxprefplugin.base.java.PsiMethodBuilder

fun LightPsiClassBuilder.addModifier(vararg modifiers: String): LightPsiClassBuilder {
    val list = modifierList
    for (modifier in modifiers) {
        list.addModifier(modifier)
    }
    return this
}

fun PsiClass.getType(): PsiType {
//    return PsiType.getTypeByName(qualifiedName!!, project, resolveScope)
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

private const val Editor = "Editor"
fun PsiClass.createEditorClass(): JavaPsiClassBuilder {
    val constructor = PsiMethodBuilder(manager, JavaLanguage.INSTANCE, Editor)
        .addModifiers(PsiModifier.PRIVATE)
        .setConstructor(true)
        .addParameter("editor", "android.content.SharedPreferences.Editor")
//    val factory = JavaPsiFacade.getElementFactory(project)
//    return factory.createClass(Editor)
//        .also {
//            it.modifierList?.setModifierProperty(PsiModifier.STATIC, true)
//            it.extendsList?.add(factory.createTypeElementFromText("io.github.sgpublic.xxpref.PrefEditor", null))
//            for (cons in it.constructors) {
//                cons.delete()
//            }
//            it.add(constructor)
//        }
    log.debug("qualifiedName: ${qualifiedName!!.split(".").last()}")
    return JavaPsiClassBuilder(this, Editor, "${qualifiedName!!.split(".").last()}.${Editor}")
        .setModifiers(PsiModifier.PUBLIC, PsiModifier.STATIC)
        .addConstructor(constructor)
        .addExtends("io.github.sgpublic.xxpref.PrefEditor")
        .setContainingClass(this)
        .also {
            it.navigationElement = this
        }
}

fun PsiClass.getEditorClass(): PsiClass {
    return findInnerClassByName(Editor, true)!!
}