package space.cubicworld.service;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayDeque;
import java.util.Deque;

public class DiscomcCommandDispatcher extends BukkitRunnable {

    private static Deque<String> commandsDeque = new ArrayDeque<>();

    @Override
    public void run() {
        while (commandsDeque.peek() != null) {
            String command = commandsDeque.pop();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }

    public static void addCommand(String command){
        commandsDeque.add(command);
    }

}
