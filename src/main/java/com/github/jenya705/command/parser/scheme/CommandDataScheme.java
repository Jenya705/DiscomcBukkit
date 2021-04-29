package com.github.jenya705.command.parser.scheme;

import com.github.jenya705.command.parser.argument.CommandArgument;
import com.github.jenya705.util.Pair;

import java.util.List;

public interface CommandDataScheme {

    List<Pair<String, CommandArgument>> getArgumentsScheme();

}
