package io.github.divinenickname.kotlin.utgen.idea.plugin

import com.intellij.openapi.project.Project
import com.squareup.kotlinpoet.FileSpec

/**
 * Compose base path for test directory. Use [toString] method for get actual path.
 */
class TestDirBasePath(
    private val project: Project,
    private val fileSpec: FileSpec,
) {

    private lateinit var basePath: String

    override fun toString(): String = generate()
        .takeIf { !::basePath.isInitialized }
        .run { basePath }

    private fun generate() {
        basePath = project.basePath!!
            .plus("/src/test/kotlin/")
            .plus(fileSpec.relativePath).removeSuffix("/${fileSpec.name}.kt")
    }
}
