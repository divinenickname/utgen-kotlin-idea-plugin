package io.github.divinenickname.kotlin.utgen.idea.plugin

import com.intellij.openapi.application.Application
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile

class VirtualDirectory(
    private val app: Application,
    private val basePath: String
) {
    private lateinit var directory: VirtualFile

    /**
     * Create new virtual directory or get it if it exists.
     */
    fun createOrGet() = app
        .runWriteAction { directory = VfsUtil.createDirectoryIfMissing(basePath)!! }
        .run { directory }
}
