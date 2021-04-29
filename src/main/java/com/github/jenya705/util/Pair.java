package com.github.jenya705.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pair<T, V> {

    private T left;
    private V right;

    public Pair(T left, V right) {
        setLeft(left);
        setRight(right);
    }

    public Pair() {}

}
