package io.github.divinenickname.kotlin.utgen.idea.plugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VirtualFileManager
import io.github.divinenickname.kotlin.utgen.core.UnitTestGenerator
import java.io.File

class GenerateUnitTest : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.getData(CommonDataKeys.VIRTUAL_FILE)!!.path
            .let(::File)
            .apply {
                UnitTestGenerator().generateAndSave(this.toPath())
            }
            .also {
                ApplicationManager.getApplication().invokeLater {
                    VirtualFileManager.getInstance().syncRefresh()
                    e.project?.guessProjectDir()?.refresh(false, true)
                        ?: throw RuntimeException("Project dir not found. Try to 'Reload From Disk'")
                }
            }

    }
}
