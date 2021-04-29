package com.github.jenya705.data.types;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter(AccessLevel.PROTECTED)
public abstract class ListValueType<T extends ValueType> implements ValueType {

    public static final String splitRegex = ",";

    private T type;

    public ListValueType(T type) {
        setType(type);
    }
    @Override
    public boolean canBeCreated(String stringValue) {
        String[] split = stringValue.split(splitRegex);
        for (String str: split) {
            if (!type.canBeCreated(str)) return false;
        }
        return true;
    }
    public abstract void addElement(List<Object> list, String string);

}
