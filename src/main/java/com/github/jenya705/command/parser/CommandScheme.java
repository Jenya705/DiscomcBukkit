package com.github.jenya705.command.parser;

import com.github.jenya705.util.Pair;

import java.util.List;

public interface CommandScheme {

    List<Pair<String, CommandArgument>> getArguments();

    Pair<String, CommandArgument> getArgument(String name);

}
