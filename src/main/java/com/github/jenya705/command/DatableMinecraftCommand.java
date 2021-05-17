package com.github.jenya705.command;

import com.github.jenya705.command.parser.CommandData;
import com.github.jenya705.util.ArrayIterator;
import org.bukkit.command.CommandSender;

public interface DatableMinecraftCommand {

    boolean execute(CommandSender sender, ArrayIterator<String> args, CommandData data);

}
