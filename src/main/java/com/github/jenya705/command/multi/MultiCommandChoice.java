package com.github.jenya705.command.multi;

public interface MultiCommandChoice {

    static MultiCommandChoice instance(String name, String value) {
        return new ImplMultiCommandChoice(name, value);
    }

    String getName();

    String getValue();

}
