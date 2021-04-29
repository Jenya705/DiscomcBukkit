package com.github.jenya705.command;

import com.github.jenya705.util.ArrayIterator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;

import java.util.*;

@Getter
@Setter(AccessLevel.PROTECTED)
public class MinecraftCommandsContainer implements MinecraftCommand {

    private Map<String, MinecraftCommand> subCommands = new HashMap<>();

    @Override
    public boolean execute(CommandSender commandSender, ArrayIterator<String> args) {
        if (!args.hasNext()) return false;
        String commandName = args.next();
        MinecraftCommand command = getSubCommand(commandName);
        if (command == null) return false;
        return command.execute(commandSender, args);
    }

    @Override
    public List<String> tab(CommandSender commandSender, ArrayIterator<String> args) {
        if (!args.hasNext()) return new ArrayList<>(getSubCommands().keySet());
        String commandName = args.next();
        MinecraftCommand command = getSubCommand(commandName);
        if (command == null) return null;
        return command.tab(commandSender, args);
    }

    public void addSubCommand(String name, MinecraftCommand command){
        getSubCommands().put(name.toLowerCase(Locale.ROOT), command);
    }

    public MinecraftCommand getSubCommand(String name) {
        return getSubCommands().getOrDefault(name.toLowerCase(Locale.ROOT), null);
    }

}
