package com.github.jenya705.command.parser;

import com.github.jenya705.data.SerializedMapData;
import com.github.jenya705.util.ArrayIterator;
import com.github.jenya705.util.Pair;

public class StringCommandData extends SchemeCommandData {

    public static final String ARGUMENT_PAIR_SPLIT_REGEX = ":";
    public static final String ARGUMENTS_SPLIT_REGEX = " ";

    public StringCommandData(CommandScheme scheme) {
        super(scheme);
    }

    public static StringCommandData create(CommandScheme scheme, ArrayIterator<String> args) {
        StringCommandData data = new StringCommandData(scheme);
        data.initialize(args.getArray(), args.getCurrent(), args.getArray().length);
        return data;
    }

    @Override
    @Deprecated
    public void initialize(String data) {
        initialize(data.split(ARGUMENTS_SPLIT_REGEX));
    }

    public CommandDataInitializeResult initialize(String[] data) {
        return initialize(data, 0, data.length);
    }

    public CommandDataInitializeResult initialize(String[] data, int begin, int end) {
        int currentOrder = 0;
        for (int i = begin; i < end; ++i) {
            String currentString = data[i];
            String[] splitCurrentString = currentString.split(ARGUMENT_PAIR_SPLIT_REGEX, 2);
            Pair<String, CommandArgument> argumentPair;
            String senderValue;
            CommandDataParseResult argumentSetResult;
            if (splitCurrentString.length == 1) { // ordered
                argumentSetResult = setArgument(currentOrder, currentString);
                if (argumentSetResult == CommandDataParseResult.TOO_MANY_ARGUMENTS) {
                    return CommandDataInitializeResult.tooManyArguments(
                            end-begin, getScheme().getArguments().size());
                }
                argumentPair = getScheme().getArguments().get(currentOrder++);
                senderValue = currentString;
            }
            else { // argument named
                argumentPair = getScheme().getArgument(splitCurrentString[0]);
                senderValue = splitCurrentString[1];
                argumentSetResult = setArgument(argumentPair, senderValue);
            }
            if (argumentSetResult != CommandDataParseResult.SUCCESS) {
                return CommandDataInitializeResult.createWithMessage(
                        argumentSetResult, argumentPair.getLeft(), senderValue,
                        argumentPair.getRight().getValueType(),
                        end-begin, getScheme().getArguments().size()
                );
            }
        }
        return fillArguments();
    }

    @Override
    public SerializedMapData<String, Object> createSelfInstance() {
        return new StringCommandData(getScheme());
    }
}
