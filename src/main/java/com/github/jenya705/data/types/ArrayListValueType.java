package com.github.jenya705.data.types;

import java.util.ArrayList;
import java.util.List;

public class ArrayListValueType<T extends ValueType> extends ListValueType<T> {

    public ArrayListValueType(T type) {
        super(type);
    }

    @Override
    public Object createInstance(String stringValue) {
        List<Object> list = new ArrayList<>();
        String[] splitStringArray = stringValue.split(ListValueType.splitRegex);
        for (String splitString: splitStringArray) {
            addElement(list, splitString);
        }
        return list;
    }

    @Override
    public void addElement(List<Object> list, String string) {
        list.add(getType().createInstance(string));
    }
}
