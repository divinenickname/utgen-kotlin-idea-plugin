package io.github.divinenickname.kotlin.utgen.idea.plugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.findOrCreateDirectory
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.impl.file.PsiDirectoryFactory
import io.github.divinenickname.kotlin.utgen.core.UnitTestGenerator
import org.jetbrains.kotlin.idea.KotlinFileType
import java.io.File

class GenerateUnitTest : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: throw RuntimeException("Project must not be null. Critical failure.")
        val code = e.getData(CommonDataKeys.VIRTUAL_FILE)!!.path.let(::File).toPath()
            .let { UnitTestGenerator().generate(it) }

        val packageToPath = "src/test/kotlin/" + code.relativePath.removeSuffix("${code.name}.kt")
        val baseDir = project.guessProjectDir()!!.findOrCreateDirectory(packageToPath)
        val fileName = "${code.name}.kt"

        val psiDirectory = PsiDirectoryFactory.getInstance(project).createDirectory(baseDir)

        ApplicationManager.getApplication().runWriteAction { psiDirectory.findFile(fileName)?.delete() }

        val psiFile = PsiFileFactory.getInstance(project)
            .createFileFromText(fileName, KotlinFileType.INSTANCE, code.toString())

        CodeFormatter(project, psiFile).apply()

        ApplicationManager.getApplication().runWriteAction {
            psiDirectory.add(psiFile)
        }
    }
}
