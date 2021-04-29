package com.github.jenya705.data.types;

import lombok.Getter;
import org.apache.commons.lang.math.NumberUtils;

import java.util.function.Function;

@Getter
public enum PrimitiveValueType implements ValueType {

    BOOLEAN(Boolean::parseBoolean, (str) -> str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false")),
    CHAR(it -> {
        if (it.length() != 1) throw new IllegalArgumentException("String length is not 1");
        return it.charAt(0);
    }, (str) -> str.length() == 1),
    BYTE(Byte::parseByte, NumberUtils::isNumber),
    SHORT(Short::parseShort, NumberUtils::isNumber),
    INTEGER(Integer::parseInt, NumberUtils::isNumber),
    LONG(Long::parseLong, NumberUtils::isNumber),
    UNSIGNED_INTEGER(Integer::parseUnsignedInt, NumberUtils::isNumber),
    UNSIGNED_LONG(Long::parseUnsignedLong, NumberUtils::isNumber),
    FLOAT(Float::parseFloat, NumberUtils::isNumber),
    DOUBLE(Double::parseDouble, NumberUtils::isNumber),
    STRING(it -> it, (str)->true);

    private final Function<String, Object> createInstanceFunction;
    private final Function<String, Boolean> canBeCreatedFunction;
    PrimitiveValueType(Function<String, Object> createInstanceFunction,
                       Function<String, Boolean> canBeCreatedFunction) {
        this.createInstanceFunction = createInstanceFunction;
        this.canBeCreatedFunction = canBeCreatedFunction;
    }
    @Override
    public boolean canBeCreated(String stringValue) {
        return getCanBeCreatedFunction().apply(stringValue);
    }
    @Override
    public Object createInstance(String stringValue) {
        return getCreateInstanceFunction().apply(stringValue);
    }
}
