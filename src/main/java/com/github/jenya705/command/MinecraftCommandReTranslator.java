package com.github.jenya705.command;

import com.github.jenya705.util.ArrayIterator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
@Setter(AccessLevel.PROTECTED)
public class MinecraftCommandReTranslator implements CommandExecutor, TabExecutor {

    private @NotNull MinecraftCommand command;

    public MinecraftCommandReTranslator(@NotNull MinecraftCommand command) {
        setCommand(command);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return getCommand().execute(sender, new ArrayIterator<>(args));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return getCommand().tab(sender, new ArrayIterator<>(args));
    }
}
