package com.github.jenya705.command;

import com.github.jenya705.util.ArrayIterator;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface MinecraftCommand {

    boolean execute(CommandSender commandSender, ArrayIterator<String> args);

    List<String> tab(CommandSender commandSender, ArrayIterator<String> args);

}
