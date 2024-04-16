package io.github.divinenickname.kotlin.utgen.idea.plugin

import com.intellij.openapi.application.Application
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import com.intellij.psi.codeStyle.CodeStyleManager
import com.squareup.kotlinpoet.FileSpec
import io.github.divinenickname.kotlin.utgen.core.UnitTestGenerator
import org.jetbrains.kotlin.idea.KotlinFileType
import java.io.File

class TestClass(
    private val app: Application,
    private val psiFileFactory: PsiFileFactory,
    private val project: Project,
    private val sourceCodePath: String,
) {

    private lateinit var generatedPsiFiles: List<PsiFile>
    private lateinit var generatedFileSpecs: List<FileSpec>

    fun makePsiFiles(): TestClass = psiFiles()
        .run { this@TestClass }

    /**
     * Generate or get fileSpecs from source
     */
    private fun fileSpecs(): List<FileSpec> = sourceCodePath.let(::File).toPath()
        .let { UnitTestGenerator().generateAll(it) }
        .apply { generatedFileSpecs = this }
        .let { generatedFileSpecs }

    private fun psiFiles(): List<PsiFile> {
        val fileSpecs = if (!::generatedFileSpecs.isInitialized) {
            fileSpecs()
        } else {
            generatedFileSpecs
        }

        return fileSpecs.map(::makePsiFile)
            .apply { this@TestClass.generatedPsiFiles = this }
    }

    private fun makePsiFile(fileSpec: FileSpec): PsiFile = psiFileFactory
        .createFileFromText("${fileSpec.name}.kt", KotlinFileType.INSTANCE, fileSpec.toString())
        .apply { KotlinCodeFormatter(CodeStyleManager.getInstance(project), this).apply() }

    fun saveAll() {
        require(::generatedPsiFiles.isInitialized)
        generatedPsiFiles.forEach(this::save)
    }

    private fun save(file: PsiFile) {
        val directory = VirtualDirectory(
            app = app,
            basePath = TestDirBasePath(project, generatedFileSpecs[0]).toString()
        ).createOrGet()

        PsiManager.getInstance(project)
            .findDirectory(directory)!!
            .takeIf { it.findFile(file.name) == null }
            ?.run { app.runWriteAction { this.add(file) } }
    }
}
