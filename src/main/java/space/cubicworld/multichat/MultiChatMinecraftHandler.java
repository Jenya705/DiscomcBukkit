package space.cubicworld.multichat;

import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcSave;
import space.cubicworld.discord.DiscordModule;

public class MultiChatMinecraftHandler implements Listener {

    @EventHandler
    public void chat(AsyncPlayerChatEvent event){
        DiscomcPlugin.getDiscomcPlugin().getServer().getScheduler().runTaskAsynchronously(
                DiscomcPlugin.getDiscomcPlugin(), () -> broadcastMessage(event));
    }

    public void broadcastMessage(AsyncPlayerChatEvent event){
        WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder();
        messageBuilder.setUsername(event.getPlayer().getDisplayName())
                .setContent(event.getMessage())
                .setAvatarUrl("https://crafatar.com/avatars/b31f5079c09d4979b02e2396e5fd9afb");
        DiscomcPlugin.getDiscomcPlugin().getMultiChatModule()
                .getWebhookClient().send(messageBuilder.build());
    }

}
