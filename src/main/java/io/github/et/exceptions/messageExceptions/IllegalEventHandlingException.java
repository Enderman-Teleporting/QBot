package io.github.et.exceptions.messageExceptions;

import io.github.et.exceptions.MessageException;

public class IllegalEventHandlingException extends MessageException {
    public IllegalEventHandlingException() {
        super();
    }

    public IllegalEventHandlingException(String message) {
        super(message);
    }

    public IllegalEventHandlingException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalEventHandlingException(Throwable cause) {
        super(cause);
    }
}
