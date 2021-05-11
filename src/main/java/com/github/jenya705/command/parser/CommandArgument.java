package com.github.jenya705.command.parser;

import com.github.jenya705.data.types.ValueType;

public interface CommandArgument {

    ValueType getValueType();

    Object getDefaultValue();

    boolean isRequired();

}
