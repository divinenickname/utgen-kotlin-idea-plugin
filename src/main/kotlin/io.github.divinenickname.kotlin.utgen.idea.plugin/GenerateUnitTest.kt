package io.github.divinenickname.kotlin.utgen.idea.plugin

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import com.squareup.kotlinpoet.FileSpec
import io.github.divinenickname.kotlin.utgen.core.UnitTestGenerator
import org.jetbrains.kotlin.idea.KotlinFileType
import java.io.File

class GenerateUnitTest : AnAction() {

    /**
     * EDT - Event Dispatch Thread. Used for responsible UI. [ActionUpdateThread.EDT]
     *
     * BGT - Background Thread. Used for long-running tasks or background processing. [ActionUpdateThread.BGT]
     *
     * @see [ActionUpdateThread] javadoc.
     */
    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: throw RuntimeException("Project must not be null. Critical failure.")
        val code = e.getData(CommonDataKeys.VIRTUAL_FILE)!!.path.let(::File).toPath()
            .let { UnitTestGenerator().generate(it) }
        val psiFile = makePsiFile(project, code)

        val basePath = project.basePath!!
            .plus("/src/test/kotlin/")
            .plus(code.relativePath).removeSuffix("/${code.name}.kt")

        var directory: VirtualFile? = null
        ApplicationManager.getApplication().runWriteAction {
            directory = VfsUtil.createDirectoryIfMissing(basePath)!!
        }

        PsiManager.getInstance(project).findDirectory(directory!!)!!
            .takeIf { it.findFile(psiFile.name) == null }
            ?.run { ApplicationManager.getApplication().runWriteAction { this.add(psiFile) } }
    }

    private fun makePsiFile(project: Project, fileSpec: FileSpec): PsiFile = PsiFileFactory.getInstance(project)
        .createFileFromText("${fileSpec.name}.kt", KotlinFileType.INSTANCE, fileSpec.toString())
        .apply { KotlinCodeFormatter(project, this).apply() }

    override fun update(e: AnActionEvent) {
        val presentation = e.presentation
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)

        presentation.isEnabledAndVisible = file?.extension == "kt"
    }
}
