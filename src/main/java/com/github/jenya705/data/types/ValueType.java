package com.github.jenya705.data.types;

public interface ValueType {


    boolean canBeCreated(String stringValue);

    Object createInstance(String stringValue);

}
