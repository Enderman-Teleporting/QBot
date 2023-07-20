package io.github.et.exceptions.messageExceptions;

import io.github.et.exceptions.MessageException;

public class IllegalNudgeDealingException extends MessageException {
    public IllegalNudgeDealingException() {
        super();
    }

    public IllegalNudgeDealingException(String message) {
        super(message);
    }

    public IllegalNudgeDealingException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalNudgeDealingException(Throwable cause) {
        super(cause);
    }
}
