package io.github.sgpublic.exspplugin.base

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.SyntheticElement
import com.intellij.psi.impl.light.LightModifierList
import com.intellij.psi.impl.light.LightPsiClassBuilder


open class PsiClassBuilder(
    context: PsiElement, simpleName: String,
    private val mQualifiedName: String,
) : LightPsiClassBuilder(context, simpleName), SyntheticElement {
    private val mModifierList: LightModifierList by lazy {
        LightModifierList(context.manager, context.language)
    }

    fun addModifier(vararg modifiers: String): LightPsiClassBuilder {
        for (modifier in modifiers) {
            mModifierList.addModifier(modifier)
        }
        return this
    }

    private var mContainingFile: PsiFile? = null

    override fun setContainingClass(containingClass: PsiClass?): LightPsiClassBuilder {
        mContainingFile = containingClass?.containingFile
        return super.setContainingClass(containingClass)
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

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as PsiClassBuilder
        return mQualifiedName == that.mQualifiedName
    }

    override fun hashCode(): Int {
        return mQualifiedName.hashCode()
    }
}
