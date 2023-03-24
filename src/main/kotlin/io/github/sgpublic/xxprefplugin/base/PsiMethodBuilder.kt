package io.github.sgpublic.xxprefplugin.base

import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.*
import com.intellij.psi.impl.light.LightMethodBuilder
import org.jetbrains.annotations.NotNull

open class PsiMethodBuilder(
    manager: PsiManager, language: Language, name: String
): LightMethodBuilder(manager, language, name), SyntheticElement {
    private var myBodyAsText: String? = null
    private var myBodyCodeBlock: PsiCodeBlock? = null
    private var myBuilderBodyFunction: ((PsiMethodBuilder) -> String)? = null

    override fun getBody(): PsiCodeBlock? {
        var bodyAsText: String? = myBodyAsText
        val builderBodyFunction: ((PsiMethodBuilder) -> String)? = myBuilderBodyFunction
        if (null == myBodyCodeBlock && (bodyAsText != null || builderBodyFunction != null)) {
            if (bodyAsText == null) {
                bodyAsText = builderBodyFunction?.invoke(this)
            }
            val elementFactory: PsiElementFactory = JavaPsiFacade.getElementFactory(project)
            myBodyCodeBlock = elementFactory.createCodeBlockFromText("{$bodyAsText}", this)
            myBodyAsText = null
            myBuilderBodyFunction = null
        }
        return myBodyCodeBlock
    }

    fun withBodyText(@NotNull codeBlockText: String, vararg args: Any): PsiMethodBuilder {
        myBodyAsText = String.format(codeBlockText, args)
        myBodyCodeBlock = null
        return this
    }
}

class JavaPsiMethodBuilder(
    manager: PsiManager, name: String
): PsiMethodBuilder(manager, JavaLanguage.INSTANCE, name)