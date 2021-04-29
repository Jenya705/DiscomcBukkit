package com.github.jenya705.command.parser.scheme;

import com.github.jenya705.command.parser.argument.CommandArgument;
import com.github.jenya705.util.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter(AccessLevel.PROTECTED)
public class VanillaCommandDataScheme implements CommandDataScheme {

    private List<Pair<String, CommandArgument>> argumentsScheme;

    @SafeVarargs
    public VanillaCommandDataScheme(Pair<String, CommandArgument>... argumentsScheme) {
        setArgumentsScheme(Arrays.asList(argumentsScheme));
    }

}
