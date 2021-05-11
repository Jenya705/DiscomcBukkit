package com.github.jenya705.command.multi;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter(AccessLevel.PROTECTED)
public class ImplMultiCommandDescription implements MultiCommandDescription {

    private String name;
    private String description;
    private List<MultiCommandOption> options;

    public ImplMultiCommandDescription() {
        options = new ArrayList<>();
    }

    @Override
    public MultiCommandDescription name(String name) {
        setName(name);
        return this;
    }

    @Override
    public MultiCommandDescription description(String description) {
        setDescription(description);
        return this;
    }

    @Override
    public MultiCommandDescription addOption(MultiCommandOption option) {
        getOptions().add(option);
        return this;
    }

    @Override
    public MultiCommandDescription addOptions(Collection<MultiCommandOption> options) {
        getOptions().addAll(options);
        return this;
    }

    @Override
    public MultiCommandDescription addOptions(MultiCommandOption... options) {
        for (MultiCommandOption option: options) getOptions().add(option);
        return this;
    }
}
