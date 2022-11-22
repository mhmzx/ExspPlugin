package io.github.sgpublic.exspplugin.base

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.SyntheticElement
import com.intellij.psi.impl.light.LightModifierList
import com.intellij.psi.impl.light.LightPsiClassBuilder
import org.jetbrains.kotlin.asJava.classes.KtLightClass
import org.jetbrains.kotlin.asJava.elements.KtLightMethod
import org.jetbrains.kotlin.idea.search.usagesSearch.constructor
import org.jetbrains.kotlin.lexer.KtModifierKeywordToken
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.allConstructors
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
        val that = other as JavaPsiClassBuilder
        return mQualifiedName == that.mQualifiedName
    }

    override fun hashCode(): Int {
        return mQualifiedName.hashCode()
    }
}