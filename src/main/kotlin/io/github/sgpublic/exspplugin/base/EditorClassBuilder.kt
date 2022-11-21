package io.github.sgpublic.exspplugin.base

import com.intellij.psi.PsiClass

class EditorClassBuilder(clazz: PsiClass): PsiClassBuilder(
    clazz, "Editor", clazz.qualifiedName ?: ""
) {
    override fun getQualifiedName(): String {
        val name = super.getQualifiedName()
            .takeIf { it.isNotBlank() }
            ?.let { return@let "$it." }
            ?: ""
        return "${name}Editor"
    }
}