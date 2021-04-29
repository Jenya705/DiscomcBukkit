package com.github.jenya705.service;

import com.github.jenya705.Discomc;
import com.github.jenya705.util.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayDeque;
import java.util.Deque;

@Getter
@Setter(AccessLevel.PROTECTED)
public class AsyncCommandsService implements DiscomcService {

    private Deque<Pair<CommandSender, String>> commandsDeque;

    @Override
    public void setup() {
        setCommandsDeque(new ArrayDeque<>());
        Discomc discomc = Discomc.getPlugin();
        discomc.getScheduler().runTaskTimer(() -> {
            while (commandsDeque.peek() != null) {
                Pair<CommandSender, String> currentPairCommand = commandsDeque.pop();
                if (currentPairCommand == null) continue;
                Bukkit.dispatchCommand(currentPairCommand.getLeft() == null ?
                        Bukkit.getConsoleSender() : currentPairCommand.getLeft(),
                        currentPairCommand.getRight());
            }
        }, discomc.getDefaultConfig().getCommandsExecutingTimer());
    }

    public static void addCommand(CommandSender commandSender, String command) {
        Discomc.getPlugin().getAsyncCommandsService().getCommandsDeque()
                .add(new Pair<>(commandSender, command));
    }

    public static void addCommand(String command) {
        addCommand(null, command);
    }

}
