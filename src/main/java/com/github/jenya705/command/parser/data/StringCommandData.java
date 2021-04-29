package com.github.jenya705.command.parser.data;

import com.github.jenya705.command.parser.argument.CommandArgument;
import com.github.jenya705.command.parser.scheme.CommandDataScheme;
import com.github.jenya705.data.SerializedMapData;
import com.github.jenya705.data.types.ListValueType;
import com.github.jenya705.data.types.ValueType;
import com.github.jenya705.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class StringCommandData extends SchemeCommandData{

    public static final String splitRegex = " ";
    public static final String argumentNameRegex = ":";

    public StringCommandData(CommandDataScheme scheme) {
        super(scheme);
    }

    @Override
    public void initialize(String data) {
        initialize(data.split(StringCommandData.splitRegex));
    }

    public StringCommandParseResult initialize(String[] data) {
        return initialize(data, 0, data.length);
    }

    public StringCommandParseResult initialize(String[] data, int begin, int end) {
        int currentOrder = 0;
        for (int i = begin; i < end; ++i) {
            try {
                String current = data[i];
                String[] splitCurrent = current.split(argumentNameRegex, 2);
                if (splitCurrent.length != 2) {
                    if (getScheme().getArgumentsScheme().size() <= currentOrder) {
                        return StringCommandParseResult.TOO_MANY_ARGUMENTS;
                    }
                    Pair<String, CommandArgument> argumentPair = getScheme().getArgumentsScheme().get(currentOrder++);
                    if (!argumentPair.getRight().getValueType().canBeCreated(current)) {
                        return StringCommandParseResult.ARGUMENT_TYPE_NOT_RIGHT;
                    }
                    if (argumentPair.getRight().getValueType() instanceof ListValueType) {
                        addListObject((ListValueType<? extends ValueType>) argumentPair.getRight().getValueType(),
                                argumentPair.getLeft(), current);
                    } else {
                        setObject(argumentPair.getLeft(), argumentPair.getRight().getValueType().createInstance(current));
                    }
                } else {
                    Pair<String, CommandArgument> argumentPair = getArgumentPair(splitCurrent[0]);
                    if (argumentPair == null) {
                        return StringCommandParseResult.GIVEN_ARGUMENT_NOT_EXIST;
                    }
                    if (!argumentPair.getRight().getValueType().canBeCreated(splitCurrent[1])) {
                        return StringCommandParseResult.ARGUMENT_TYPE_NOT_RIGHT;
                    }
                    if (argumentPair.getRight().getValueType() instanceof ListValueType) {
                        addListObject((ListValueType<? extends ValueType>) argumentPair.getRight().getValueType(),
                                argumentPair.getLeft(), splitCurrent[1]);
                    } else {
                        setObject(argumentPair.getLeft(),
                                argumentPair.getRight().getValueType().createInstance(splitCurrent[1]));
                    }
                }
            } catch (NumberFormatException e) {
                return StringCommandParseResult.ARGUMENT_TYPE_NOT_RIGHT;
            }
        }
        for (Pair<String, CommandArgument> argumentPair: getScheme().getArgumentsScheme()) {
            if (!getData().containsKey(argumentPair.getLeft())) {
                if (argumentPair.getRight().isRequired()) {
                    return StringCommandParseResult.REQUIRED_ARGUMENT_NOT_GIVEN;
                }
                else {
                    setObject(argumentPair.getLeft().toLowerCase(Locale.ROOT),
                            argumentPair.getRight().getDefaultValue());
                }
            }
        }
        return StringCommandParseResult.SUCCESS;
    }

    @SuppressWarnings("unchecked")
    public void addListObject(ListValueType<?> valueType, String key, String string) {
        List<Object> array;
        if (getData().containsKey(key)) array = (List<Object>) getData().get(key);
        else {
            array = new ArrayList<>();
            getData().put(key, array);
        }
        valueType.addElement(array, string);
    }

    @Override
    public SerializedMapData<String, Object> createSelfInstance() {
        return new StringCommandData(getScheme());
    }
}
