package io.github.et


class MCServer(
    val name: String,
    val workingDir: String,
    val command: String,
    val group: Long,
    val rcon_port:Int,
    val rcon_password: String?
)
