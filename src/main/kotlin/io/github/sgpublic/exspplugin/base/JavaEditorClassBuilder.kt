package io.github.sgpublic.exspplugin.base

import com.intellij.psi.PsiClass

class JavaEditorClassBuilder(clazz: PsiClass): JavaPsiClassBuilder(
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