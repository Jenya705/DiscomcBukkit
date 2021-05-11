package com.github.jenya705.command.parser;

import com.github.jenya705.util.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Locale;

@Getter
@Setter(AccessLevel.PROTECTED)
public class CommandSchemeImpl implements CommandScheme {

    private List<Pair<String, CommandArgument>> arguments;

    public CommandSchemeImpl(List<Pair<String, CommandArgument>> arguments) {
        setArguments(arguments);
    }

    @Override
    public Pair<String, CommandArgument> getArgument(String name) {
        String loweredName = name.toLowerCase(Locale.ROOT);
        for (Pair<String, CommandArgument> argumentPair: getArguments()) {
            if (argumentPair.getLeft().equals(loweredName)) {
                return argumentPair;
            }
        }
        return null;
    }
}
