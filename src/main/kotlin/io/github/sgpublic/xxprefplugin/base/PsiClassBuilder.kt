package io.github.sgpublic.xxprefplugin.base

import com.intellij.psi.*
import com.intellij.psi.impl.light.LightModifierList
import com.intellij.psi.impl.light.LightPsiClassBuilder
import java.util.*

interface PsiClassBuilder

open class JavaPsiClassBuilder(
    context: PsiElement, simpleName: String,
    private val mQualifiedName: String,
) : LightPsiClassBuilder(context, simpleName), SyntheticElement, PsiClassBuilder {
    private val mModifierList: LightModifierList by lazy {
        LightModifierList(context.manager, context.language)
    }

    fun addModifier(vararg modifiers: String): JavaPsiClassBuilder {
        for (modifier in modifiers) {
            mModifierList.addModifier(modifier)
        }
        return this
    }

    private val constructors: LinkedList<PsiMethod> = LinkedList()
    fun addConstructor(vararg methods: PsiMethod): JavaPsiClassBuilder {
        constructors.addAll(methods)
        return this
    }

    override fun getConstructors(): Array<PsiMethod> {
        return constructors.toTypedArray()
    }

    private var mContainingFile: PsiFile? = null

    override fun setContainingClass(containingClass: PsiClass?): JavaPsiClassBuilder {
        mContainingFile = containingClass?.containingFile
        super.setContainingClass(containingClass)
        return this
    }

    fun addParameterType(types: PsiTypeParameterList): JavaPsiClassBuilder {
        for (type in types.typeParameters) {
            addParameterType(type)
        }
        return this
    }

    fun addParameterType(type: PsiTypeParameter): JavaPsiClassBuilder {
        typeParameterList.addParameter(type)
        return this
    }

    override fun getContainingFile(): PsiFile? {
        return mContainingFile
    }

    override fun getModifierList(): LightModifierList {
        return mModifierList
    }

    override fun getScope(): PsiElement {
        return containingClass?.scope ?: super.getScope()
    }

    override fun getParent(): PsiElement? {
        return containingClass
    }

    override fun getQualifiedName(): String {
        return mQualifiedName
    }

    fun addExtends(type: String): JavaPsiClassBuilder {
        extendsList.addReference(type)
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as JavaPsiClassBuilder
        return mQualifiedName == that.mQualifiedName
    }

    override fun hashCode(): Int {
        return mQualifiedName.hashCode()
    }
}