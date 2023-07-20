package io.github.et.exceptions.messageExceptions;

import io.github.et.exceptions.MessageException;

public class IllegalMessageDealingException extends MessageException {
    public IllegalMessageDealingException() {
        super();
    }

    public IllegalMessageDealingException(String message) {
        super(message);
    }

    public IllegalMessageDealingException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalMessageDealingException(Throwable cause) {
        super(cause);
    }
}
