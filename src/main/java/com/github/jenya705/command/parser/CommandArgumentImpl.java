package com.github.jenya705.command.parser;

import com.github.jenya705.data.types.ValueType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommandArgumentImpl implements CommandArgument {

    private ValueType valueType;
    private boolean required;
    private Object defaultValue;

    public static CommandArgumentImpl required(ValueType valueType) {
        return new CommandArgumentImpl(valueType, true, null);
    }

    public static CommandArgumentImpl notRequired(ValueType valueType) {
        return new CommandArgumentImpl(valueType, false, null);
    }

    public static CommandArgumentImpl notRequired(ValueType valueType, Object defaultValue) {
        return new CommandArgumentImpl(valueType, false, defaultValue);
    }

}
