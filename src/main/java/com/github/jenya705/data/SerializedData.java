package com.github.jenya705.data;

import java.util.List;

public interface SerializedData {

    void initialize(String data);

    Object getObject(Object key);

    Boolean getBoolean(Object key);

    String getString(Object key);

    Byte getByte(Object key);

    Short getShort(Object key);

    Integer getInteger(Object key);

    Long getLong(Object key);

    Float getFloat(Object key);

    Double getDouble(Object key);

    List<Object> getList(Object key);

    SerializedData getDirectory(Object key);

    Object getObject(Object key, Object defaultValue);

    Boolean getBoolean(Object key, boolean defaultValue);

    String getString(Object key, String defaultValue);

    Byte getByte(Object key, byte defaultValue);

    Short getShort(Object key, short defaultValue);

    Integer getInteger(Object key, int defaultValue);

    Long getLong(Object key, long defaultValue);

    Float getFloat(Object key, float defaultValue);

    Double getDouble(Object key, double defaultValue);

    List<Object> getList(Object key, List<Object> defaultValue);

    SerializedData getDirectory(Object key, SerializedData defaultValue);

    String toSerializedString();

    void setObject(Object key, Object value);

}
