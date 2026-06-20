package io.github.et.utils.bilibili;

public class BilibiliRequestException extends RuntimeException {
    public BilibiliRequestException(String message) {
        super(message);
    }
    public BilibiliRequestException(String message, Throwable cause) {
        super(message, cause);
    }
    public BilibiliRequestException(Throwable cause) {
        super(cause);
    }
    public BilibiliRequestException() {
        super();
    }

}
