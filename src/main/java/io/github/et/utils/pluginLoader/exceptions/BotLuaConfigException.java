package io.github.et.utils.pluginLoader.exceptions;

public class BotLuaConfigException extends Exception{
    public BotLuaConfigException() {
        super();
    }
    public BotLuaConfigException(String message) {
        super(message);
    }
    public BotLuaConfigException(String message, Throwable cause) {
        super(message, cause);
    }
    public BotLuaConfigException(Throwable cause) {
        super(cause);
    }
}
