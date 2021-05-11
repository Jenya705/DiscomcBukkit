package com.github.jenya705.command.multi;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter(AccessLevel.PROTECTED)
public class ImplMultiCommandOption implements MultiCommandOption {

    private String name;
    private String description;
    private MultiCommandOptionType type;
    private boolean required;
    private List<MultiCommandOption> options;
    private List<MultiCommandChoice> choices;

    public ImplMultiCommandOption() {
        setOptions(new ArrayList<>());
        setChoices(new ArrayList<>());
    }

    @Override
    public MultiCommandOption name(String name) {
        setName(name);
        return this;
    }

    @Override
    public MultiCommandOption description(String description) {
        setDescription(description);
        return this;
    }

    @Override
    public MultiCommandOption type(MultiCommandOptionType type) {
        setType(type);
        return this;
    }

    @Override
    public MultiCommandOption required(boolean required) {
        setRequired(required);
        return this;
    }

    @Override
    public MultiCommandOption addOption(MultiCommandOption option) {
        getOptions().add(option);
        return this;
    }

    @Override
    public MultiCommandOption addOptions(Collection<MultiCommandOption> options) {
        getOptions().addAll(options);
        return this;
    }

    @Override
    public MultiCommandOption addOptions(MultiCommandOption... options) {
        for (MultiCommandOption option: options) getOptions().add(option);
        return this;
    }

    @Override
    public MultiCommandOption addChoice(MultiCommandChoice choice) {
        getChoices().add(choice);
        return this;
    }

    @Override
    public MultiCommandOption addChoices(Collection<MultiCommandChoice> choices) {
        getChoices().addAll(choices);
        return this;
    }

    @Override
    public MultiCommandOption addChoices(MultiCommandChoice... choices) {
        for (MultiCommandChoice choice: choices) getChoices().add(choice);
        return this;
    }
}
