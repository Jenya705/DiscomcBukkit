package com.github.jenya705.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;

@Getter
@Setter(AccessLevel.PROTECTED)
public class ArrayIterator<T> implements Iterator<T> {

    private T[] array;
    private int current;

    public ArrayIterator(T[] array){
        setArray(array);
    }

    @Override
    public boolean hasNext() {
        return current < array.length;
    }

    public boolean hasNext(int i) {
        return current + i <= array.length;
    }

    @Override
    public T next() {
        if (hasNext()) {
            return array[current++];
        }
        throw new ArrayIndexOutOfBoundsException(String.format(
                "Array length more than index: %s >= %s", getCurrent(), getArray().length));
    }
}
