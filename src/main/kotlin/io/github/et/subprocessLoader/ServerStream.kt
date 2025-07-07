package io.github.et.subprocessLoader

import io.github.et.Main
import java.io.InputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket

class ServerStream {
    var serverSocket: ServerSocket = ServerSocket(Main.JSON_NO_GUIDE.getJSONObject("Global").getInteger("port2"))

    init {
        val process = Runtime.getRuntime().exec(
            arrayOf(
                "java",
                "-Dfile.encoding=utf-8",
                "-jar",
                "./SubProcess.jar",
                Main.JSON_NO_GUIDE.getJSONObject("Global").getInteger("port2").toString()
            )
        )

        accepted = serverSocket.accept()

        `is` = accepted.getInputStream()
        os = accepted.getOutputStream()
    }

    companion object {
        lateinit var `is`: InputStream
        lateinit var os: OutputStream
        lateinit var accepted:Socket
    }
}