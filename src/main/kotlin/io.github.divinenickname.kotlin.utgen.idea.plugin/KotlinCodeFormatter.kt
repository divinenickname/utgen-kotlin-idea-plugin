package io.github.divinenickname.kotlin.utgen.idea.plugin

import com.intellij.openapi.application.Application
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtFunction

/**
 * Default code formatter. Use it to remove trailing spaces, useless 'public' naming and other unnecessary things.
 */
class KotlinCodeFormatter(private val project: Project, private val psiFile: PsiFile) {

    /**
     * Already wrapped with [WriteCommandAction.runWriteCommandAction] and
     * [Application.runWriteAction]
     */
    fun apply() {
        (psiFile as KtFile).declarations.forEach { declaration ->
            if (declaration is KtClassOrObject) {
                declaration.declarations.filterIsInstance<KtFunction>()
                    .filter { it.hasModifier(KtTokens.PUBLIC_KEYWORD) }
                    .forEach { it.removeModifier(KtTokens.PUBLIC_KEYWORD) }
            }
        }

        CodeStyleManager.getInstance(project).reformat(psiFile)
    }
}
