package com.github.jenya705.command.parser.data;

import com.github.jenya705.command.parser.argument.CommandArgument;
import com.github.jenya705.command.parser.scheme.CommandDataScheme;
import com.github.jenya705.util.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PROTECTED)
public abstract class SchemeCommandData extends CommandData {

    private CommandDataScheme scheme;

    public SchemeCommandData(CommandDataScheme scheme) {
        setScheme(scheme);
    }

    public Pair<String, CommandArgument> getArgumentPair(String argumentName){
        for (Pair<String, CommandArgument> argumentPair: getScheme().getArgumentsScheme()) {
            if (argumentPair.getLeft().equalsIgnoreCase(argumentName)) return argumentPair;
        }
        return null;
    }

}
