package com.github.jenya705.data;

public interface MinecraftConfigData {

    String getMessage(Object key, boolean prefixed);

    String getMessage(Object key, String defaultMessage, boolean prefixed);

    void setMessage(Object key, String message, boolean prefixed);

}
