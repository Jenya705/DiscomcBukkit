package com.github.jenya705.command.multi;

import java.util.Collection;
import java.util.List;

public interface MultiCommandOption {

    static MultiCommandOption instance() {
        return new ImplMultiCommandOption();
    }

    MultiCommandOption name(String name);

    MultiCommandOption description(String description);

    MultiCommandOption type(MultiCommandOptionType type);

    MultiCommandOption required(boolean required);

    MultiCommandOption addOption(MultiCommandOption option);

    MultiCommandOption addOptions(Collection<MultiCommandOption> options);

    MultiCommandOption addOptions(MultiCommandOption... options);

    MultiCommandOption addChoice(MultiCommandChoice choice);

    MultiCommandOption addChoices(Collection<MultiCommandChoice> choices);

    MultiCommandOption addChoices(MultiCommandChoice... choices);

    String getName();

    String getDescription();

    MultiCommandOptionType getType();

    boolean isRequired();

    List<MultiCommandOption> getOptions();

    List<MultiCommandChoice> getChoices();

}
