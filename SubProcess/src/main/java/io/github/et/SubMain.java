package io.github.et;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

public class SubMain {
    private static final Map<String, Process> processMap = new ConcurrentHashMap<>();
    private static final Map<String, Boolean> isGracefulShutdown = new ConcurrentHashMap<>();
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        runWithPort(port);
    }

    public static void runWithPort(int port) {
        try (Socket socket = new Socket("127.0.0.1", port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            executor.submit(() -> handleServerInput(reader, writer));
            try {
                socket.getInputStream().read();
            } catch (IOException ignored) {}
        } catch (IOException e) {
        } finally {
            shutdownAll();
            executor.shutdown();
        }
    }

    private static void handleServerInput(BufferedReader reader, BufferedWriter writer) {
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                int rightBracket = line.indexOf("]");
                if (!line.startsWith("[") || rightBracket < 0) {
                    continue;
                }
                String name = line.substring(1, rightBracket);
                String content = line.substring(rightBracket + 1);
                if (content.startsWith(" ")) content = content.substring(1);
                if (content.contains("启动命令")) {
                    String command = content.replaceFirst("启动命令", "").trim();
                    startProcess(name, command, writer);
                } else {
                    sendToProcess(name, content);
                }
            }
        } catch (IOException e) {
        }
    }

    private static void startProcess(String name, String command, BufferedWriter writer) {
        executor.submit(() -> {
            try {
                ProcessBuilder pb = new ProcessBuilder(parseCommand(command));
                pb.redirectErrorStream(true);
                Process process = pb.start();
                processMap.put(name, process);
                isGracefulShutdown.put(name, !name.isEmpty());
                executor.submit(() -> {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            synchronized (writer) {
                                writer.write("[" + name + "]" + line + "\r\n");
                                writer.flush();
                            }
                        }
                    } catch (IOException ignored) {}
                });
                process.waitFor();
                processMap.remove(name);
                isGracefulShutdown.remove(name);
            } catch (Exception e) {
            }
        });
    }

    private static void sendToProcess(String name, String message) {
        Process process = processMap.get(name);
        if (process == null) return;
        try {
            OutputStream os = process.getOutputStream();
            os.write((message + "\r\n").getBytes());
            os.flush();
        } catch (IOException ignored) {}
    }

    private static List<String> parseCommand(String command) {
        List<String> result = new ArrayList<>();
        boolean inQuote = false;
        StringBuilder sb = new StringBuilder();
        for (char c : command.toCharArray()) {
            if (c == '"') inQuote = !inQuote;
            else if (c == ' ' && !inQuote) {
                if (sb.length() > 0) {
                    result.add(sb.toString());
                    sb.setLength(0);
                }
            } else sb.append(c);
        }
        if (sb.length() > 0) result.add(sb.toString());
        return result;
    }

    private static void shutdownAll() {
        for (String name : new ArrayList<>(processMap.keySet())) {
            shutdownProcess(name);
        }
    }

    private static void shutdownProcess(String name) {
        Process process = processMap.get(name);
        if (process == null) return;
        boolean graceful = isGracefulShutdown.getOrDefault(name, false);
        if (graceful) {
            try {
                OutputStream os = process.getOutputStream();
                os.write("stop\r\n".getBytes());
                os.flush();
            } catch (IOException ignored) {}
            executor.submit(() -> {
                try {
                    if (!process.waitFor(30, TimeUnit.SECONDS)) {
                        process.destroyForcibly();
                    }
                } catch (InterruptedException ignored) {}
            });
        } else {
            process.destroyForcibly();
        }
    }
}
