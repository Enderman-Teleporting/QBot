package io.github.et.exceptions;

public class BotInfoNotFoundException extends Exception {
    public BotInfoNotFoundException() {
        super();
    }

    public BotInfoNotFoundException(String message) {
        super(message);
    }

    public BotInfoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BotInfoNotFoundException(Throwable cause) {
        super(cause);
    }

}
