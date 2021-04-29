package com.github.jenya705.command.parser.data;

import com.github.jenya705.data.SerializedMapData;

public abstract class CommandData extends SerializedMapData<String, Object> {

    @Override
    public String toSerializedString() {
        throw new UnsupportedOperationException("Is not possible with command data");
    }

}
