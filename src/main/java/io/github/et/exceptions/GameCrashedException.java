package io.github.et.exceptions;

public class GameCrashedException extends Exception {
    public GameCrashedException() {
        super();
    }

    public GameCrashedException(String message) {
        super(message);
    }

    public GameCrashedException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameCrashedException(Throwable cause) {
        super(cause);
    }
}
