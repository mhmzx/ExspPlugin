package io.github.sgpublic.xxprefplugin.base

import com.intellij.psi.PsiClass

class JavaEditorClassBuilder(clazz: PsiClass): JavaPsiClassBuilder(
    clazz, Name, clazz.qualifiedName ?: ""
) {
    companion object {
        const val Name = "Editor"
    }
    override fun getQualifiedName(): String {
        val name = super.getQualifiedName()
            .takeIf { it.isNotBlank() }
            ?.let { return@let "$it." }
            ?: ""
        return "${name}${Name}"
    }
}