package io.github.et

import lombok.Getter
import lombok.Setter

@Getter
@Setter
class MCServer(
    val name: String,
    val workingDir: String,
    val command: String,
    private val group: Long
)
