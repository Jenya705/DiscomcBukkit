package com.github.jenya705.command.parser;

import com.github.jenya705.data.types.ValueType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PROTECTED)
public class DescribedCommandArgument extends CommandArgumentImpl {

    private String name;
    private String description;

    public DescribedCommandArgument(ValueType valueType, boolean required, Object defaultValue, String name, String description) {
        super(valueType, required, defaultValue);
        setName(name);
        setDescription(description);
    }
}
