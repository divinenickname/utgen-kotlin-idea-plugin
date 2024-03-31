package io.github.divinenickname.kotlin.utgen.idea.plugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import com.intellij.psi.codeStyle.CodeStyleManager
import com.squareup.kotlinpoet.FileSpec
import io.github.divinenickname.kotlin.utgen.core.UnitTestGenerator
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtFunction
import java.io.File

class GenerateUnitTest : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: throw RuntimeException("Project must not be null. Critical failure.")
        val code = e.getData(CommonDataKeys.VIRTUAL_FILE)!!.path.let(::File).toPath()
            .let { UnitTestGenerator().generate(it) }
        val psiFile = makePsiFile(project, code)

        val basePath = project.basePath!!
            .plus("/src/test/kotlin/")
            .plus(code.relativePath).removeSuffix("/${code.name}.kt")

        val directory = VfsUtil.createDirectoryIfMissing(basePath)!!
        PsiManager.getInstance(project).findDirectory(directory)!!
            .takeIf { it.findFile(psiFile.name) == null }
            ?.run {
                ApplicationManager.getApplication().runWriteAction { this.add(psiFile) }
            }
    }

    private fun makePsiFile(project: Project, fileSpec: FileSpec): PsiFile {
        val fileName = "${fileSpec.name}.kt"
        val psiFile =  PsiFileFactory.getInstance(project)
            .createFileFromText(fileName, KotlinFileType.INSTANCE, fileSpec.toString())

        (psiFile as KtFile).declarations.forEach { declaration ->
            if (declaration is KtClassOrObject) {
                declaration.declarations.filterIsInstance<KtFunction>()
                    .filter { it.hasModifier(KtTokens.PUBLIC_KEYWORD) }
                    .forEach { it.removeModifier(KtTokens.PUBLIC_KEYWORD) }
            }
        }
        CodeStyleManager.getInstance(project).reformat(psiFile)

        return psiFile
    }

    override fun update(e: AnActionEvent) {
        val presentation = e.presentation
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)

        presentation.isEnabledAndVisible = file?.extension == "kt"
    }
}
