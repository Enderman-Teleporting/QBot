package io.github.et.subprocessLoader;

import io.github.et.Main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerStream {
    ServerSocket serverSocket;
    InputStream is;
    OutputStream os;
    public ServerStream() throws IOException {
        serverSocket = new ServerSocket(Main.JSON_NO_GUIDE.getJSONObject("Global").getInteger("port2"));
        Process process = Runtime.getRuntime().exec(new String[]{"java", "-Dfile.encoding=utf-8", "-jar", "\"./SubProcess.jar\"", String.valueOf(Main.JSON_NO_GUIDE.getInteger("port2"))});
        Socket accepted=serverSocket.accept();
        is=accepted.getInputStream();
        os=accepted.getOutputStream();
    }
}