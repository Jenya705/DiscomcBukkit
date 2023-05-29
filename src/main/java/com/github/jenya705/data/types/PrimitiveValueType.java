package com.github.jenya705.data.types;

import lombok.Getter;

import java.util.function.Function;

@Getter
public enum PrimitiveValueType implements ValueType {

    BOOLEAN(Boolean::parseBoolean, (str) -> str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false")),
    CHAR(it -> {
        if (it.length() != 1) throw new IllegalArgumentException("String length is not 1");
        return it.charAt(0);
    }, (str) -> str.length() == 1),
    BYTE(Byte::parseByte, PrimitiveValueType::isNumber),
    SHORT(Short::parseShort, PrimitiveValueType::isNumber),
    INTEGER(Integer::parseInt, PrimitiveValueType::isNumber),
    LONG(Long::parseLong, PrimitiveValueType::isNumber),
    UNSIGNED_INTEGER(Integer::parseUnsignedInt, PrimitiveValueType::isNumber),
    UNSIGNED_LONG(Long::parseUnsignedLong, PrimitiveValueType::isNumber),
    FLOAT(Float::parseFloat, PrimitiveValueType::isNumber),
    DOUBLE(Double::parseDouble, PrimitiveValueType::isNumber),
    STRING(it -> it, (str)->true);

    private final Function<String, Object> createInstanceFunction;
    private final Function<String, Boolean> canBeCreatedFunction;
    PrimitiveValueType(Function<String, Object> createInstanceFunction,
                       Function<String, Boolean> canBeCreatedFunction) {
        this.createInstanceFunction = createInstanceFunction;
        this.canBeCreatedFunction = canBeCreatedFunction;
    }

    private static boolean isNumber(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (Exception e) {
            return false;
        }
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
