package com.github.jenya705.command.parser.argument;

import com.github.jenya705.data.types.ValueType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PROTECTED)
public class VanillaCommandArgument implements CommandArgument {

    private Object defaultValue;
    private ValueType valueType;
    private boolean required;

    /**
     * required will be true automatically
     * @param valueType valueType
     */
    public VanillaCommandArgument(ValueType valueType) {
        this(valueType, true);
    }

    /**
     * defaultValue will be null automatically
     * @param valueType value type
     * @param required is argument required
     */
    public VanillaCommandArgument(ValueType valueType, boolean required) {
        this(valueType, required, null);
    }

    /**
     * required will be false automatically
     * @param valueType value type
     * @param defaultValue is argument required
     */
    public VanillaCommandArgument(ValueType valueType, Object defaultValue) {
        this(valueType, false, defaultValue);
    }

    /**
     * @param valueType value type
     * @param required is argument required
     * @param defaultValue default value of argument
     */
    public VanillaCommandArgument(ValueType valueType, boolean required, Object defaultValue) {
        setValueType(valueType);
        setRequired(required);
        setDefaultValue(defaultValue);
    }

}
