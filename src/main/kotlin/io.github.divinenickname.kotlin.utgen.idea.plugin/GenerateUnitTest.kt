package io.github.divinenickname.kotlin.utgen.idea.plugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import io.github.divinenickname.kotlin.utgen.core.UnitTestGenerator
import java.io.File

class GenerateUnitTest : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.getData(CommonDataKeys.VIRTUAL_FILE)!!.path
            .let(::File)
            .apply {
                UnitTestGenerator().generateAndSave(this.toPath())
            }
    }
}
