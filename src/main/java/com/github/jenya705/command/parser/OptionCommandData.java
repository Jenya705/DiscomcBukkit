package com.github.jenya705.command.parser;

import com.github.jenya705.data.SerializedMapData;
import com.github.jenya705.util.Pair;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;

import java.util.List;
import java.util.Optional;

public class OptionCommandData extends SchemeCommandData {

    public OptionCommandData(CommandScheme scheme) {
        super(scheme);
    }

    @Override
    public void initialize(String data) {
        throw new UnsupportedOperationException("OptionCommandData not support string data");
    }

    public CommandDataInitializeResult initialize(List<ApplicationCommandInteractionOption> options) {
        for (ApplicationCommandInteractionOption option: options) {
            Pair<String, CommandArgument> argumentPair = getScheme().getArgument(option.getName());
            Optional<ApplicationCommandInteractionOptionValue> value = option.getValue();
            if (value.isPresent()) {
                if (argumentPair.getRight().getValueType().canBeCreated(value.get().getRaw())) {
                    setObject(argumentPair.getLeft(), argumentPair.getRight().getValueType().createInstance(value.get().getRaw()));
                }
                else {
                    return CommandDataInitializeResult.argumentTypeNotRight(
                            argumentPair.getLeft(), value.get().getRaw(), argumentPair.getRight().getValueType());
                }
            }
            else {
                setObject(argumentPair.getLeft(), argumentPair.getRight().getDefaultValue());
            }
        }
        return new CommandDataInitializeResult(CommandDataParseResult.SUCCESS, null);
    }

    @Override
    public SerializedMapData<String, Object> createSelfInstance() {
        return new OptionCommandData(getScheme());
    }
}
