package io.github.sgpublic.exspplugin.base

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.SyntheticElement
import com.intellij.psi.impl.light.LightModifierList
import com.intellij.psi.impl.light.LightPsiClassBuilder
import java.util.*


open class PsiClassBuilder(
    context: PsiElement, simpleName: String,
    private val mQualifiedName: String,
) : LightPsiClassBuilder(context, simpleName), SyntheticElement {
    private val mModifierList: LightModifierList by lazy {
        LightModifierList(context.manager, context.language)
    }

    fun addModifier(vararg modifiers: String): PsiClassBuilder {
        for (modifier in modifiers) {
            mModifierList.addModifier(modifier)
        }
        return this
    }

    private val constructors: LinkedList<PsiMethod> = LinkedList()
    fun addConstructor(vararg methods: PsiMethod): PsiClassBuilder {
        constructors.addAll(methods)
        return this
    }

    override fun getConstructors(): Array<PsiMethod> {
        return constructors.toTypedArray()
    }

    private var mContainingFile: PsiFile? = null

    override fun setContainingClass(containingClass: PsiClass?): PsiClassBuilder {
        mContainingFile = containingClass?.containingFile
        super.setContainingClass(containingClass)
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
