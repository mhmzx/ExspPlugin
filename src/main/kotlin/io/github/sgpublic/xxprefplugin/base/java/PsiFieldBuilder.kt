package io.github.sgpublic.xxprefplugin.base.java

import com.intellij.psi.*
import com.intellij.psi.impl.light.LightFieldBuilder
import com.intellij.psi.impl.light.LightModifierList

/**
 * @author sgpublic
 * @Date 2023/3/27 11:20
 */
class PsiFieldBuilder(
    manager: PsiManager, private val name: String, type: PsiType
): LightFieldBuilder(manager, name, type) {
    fun addModifiers(vararg modifiers: String): PsiFieldBuilder {
        val modifierList = modifierList as LightModifierList
        for (modifier in modifiers) {
            modifierList.addModifier(modifier)
        }
        return this
    }


    private var mContainingFile: PsiFile? = null

    override fun setContainingClass(containingClass: PsiClass?): LightFieldBuilder {
        mContainingFile = containingClass?.containingFile
        super.setContainingClass(containingClass)
        return this
    }

    override fun getContainingFile(): PsiFile? {
        return mContainingFile
    }

    override fun getParent(): PsiElement? {
        return containingClass
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as PsiFieldBuilder
        return name == that.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}