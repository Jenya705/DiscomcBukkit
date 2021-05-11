package com.github.jenya705.command.parser;

import com.github.jenya705.util.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
@Setter(AccessLevel.PROTECTED)
public class CommandArgumentsBuilder {

    private List<Pair<String, CommandArgument>> arguments = new ArrayList<>();

    public static CommandArgumentsBuilder instance() {
        return new CommandArgumentsBuilder();
    }

    public CommandArgumentsBuilder add(String name, CommandArgument argument) {
        arguments.add(new Pair<>(name.toLowerCase(Locale.ROOT), argument));
        return this;
    }

    public CommandScheme asScheme() {
        return new CommandSchemeImpl(getArguments());
    }

}
