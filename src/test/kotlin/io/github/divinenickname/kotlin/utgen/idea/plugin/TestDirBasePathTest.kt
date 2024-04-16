package io.github.divinenickname.kotlin.utgen.idea.plugin

import com.intellij.openapi.project.Project
import com.squareup.kotlinpoet.FileSpec
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

internal class TestDirBasePathTest {

    @Test
    fun toStringTest() {
        val project: Project = mock()
        val fileSpec = FileSpec
            .builder("abc.fizz", "Testclass.kt")
            .build()

        whenever(project.basePath).thenReturn("/path/to/file")

        val obj = TestDirBasePath(project, fileSpec)

        val expected = "/path/to/file/src/test/kotlin/abc/fizz"
        val actual = obj.toString()

        Assertions.assertEquals(expected, actual)
    }
}
