package io.github.et


class MCServer(
    val name: String,
    val workingDir: String,
    val command: String,
    val group: Long,
    val useAutoBackup: Boolean,
    val useDeathMessage:Boolean,
    val useAdvancement: Boolean,
    var encoding: String ="UTF-8"
)
