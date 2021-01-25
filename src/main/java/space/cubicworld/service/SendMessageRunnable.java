package space.cubicworld.service;

import space.cubicworld.multichat.MinecraftChatHandler;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SendMessageRunnable extends BukkitRunnable {

    private Player player;
    private String message;

    public SendMessageRunnable(Player player, String message) {
        this.player = player;
        this.message = message;
    }

    @Override
    public void run() {
        MinecraftChatHandler.sendMessage(player, message);
    }

}
