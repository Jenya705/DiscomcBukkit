package com.github.jenya705.command.parser.argument;

import com.github.jenya705.data.types.ValueType;

public interface CommandArgument {

    /**
     * @return value which will be used if argument is not given
     */
    Object getDefaultValue();

    ValueType getValueType();

    boolean isRequired();

}
