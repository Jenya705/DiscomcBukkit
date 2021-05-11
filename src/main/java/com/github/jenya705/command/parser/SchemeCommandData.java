package com.github.jenya705.command.parser;

import com.github.jenya705.data.types.ListValueType;
import com.github.jenya705.data.types.ValueType;
import com.github.jenya705.util.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter(AccessLevel.PROTECTED)
public abstract class SchemeCommandData extends CommandData{

    private CommandScheme scheme;

    public SchemeCommandData(CommandScheme scheme) {
        setScheme(scheme);
    }

    public CommandDataParseResult setArgument(String name, String stringValue) {
        return setArgument(getScheme().getArgument(name), stringValue);
    }

    public CommandDataParseResult setArgument(int order, String stringValue) {
        if (getScheme().getArguments().size() <= order) return CommandDataParseResult.TOO_MANY_ARGUMENTS;
        return setArgument(getScheme().getArguments().get(order), stringValue);
    }

    @SuppressWarnings("Unchecked")
    public CommandDataParseResult setArgument(Pair<String, CommandArgument> argumentPair, String stringValue) {
        if (argumentPair == null) return CommandDataParseResult.GIVEN_ARGUMENT_NOT_EXIST;
        CommandArgument commandArgument = argumentPair.getRight();
        ValueType commandArgumentValueType = commandArgument.getValueType();
        if (commandArgumentValueType instanceof ListValueType) {
            ListValueType<?> commandArgumentListValueType = (ListValueType<?>) commandArgumentValueType;
            if (!commandArgumentListValueType.getType().canBeCreated(stringValue)) {
                return CommandDataParseResult.ARGUMENT_TYPE_NOT_RIGHT;
            }
            if (!getData().containsKey(argumentPair.getLeft())) setObject(argumentPair.getLeft(), new ArrayList<>());
            commandArgumentListValueType.addElement((List<Object>) getObject(argumentPair.getLeft()), stringValue);
        }
        else {
            if (!commandArgumentValueType.canBeCreated(stringValue)) {
                return CommandDataParseResult.ARGUMENT_TYPE_NOT_RIGHT;
            }
            setObject(argumentPair.getLeft(), commandArgumentValueType.createInstance(stringValue));
        }
        return CommandDataParseResult.SUCCESS;
    }

    public CommandDataInitializeResult fillArguments() {
        for (Pair<String, CommandArgument> argumentPair: getScheme().getArguments()) {
            if (getData().containsKey(argumentPair.getLeft())) continue;
            CommandArgument commandArgument = argumentPair.getRight();
            if (commandArgument.isRequired()) {
                return CommandDataInitializeResult.requiredArgumentNotGiven(
                        argumentPair.getLeft(), commandArgument.getValueType());
            }
            setObject(argumentPair.getLeft(), commandArgument.getDefaultValue());
        }
        return new CommandDataInitializeResult(CommandDataParseResult.SUCCESS, null);
    }

}
