package space.cubicworld.multichat;

import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcUtil;

public class MultiChatMinecraftHandler implements Listener {

    @EventHandler
    public void chat(AsyncPlayerChatEvent event){
        DiscomcPlugin.getDiscomcPlugin().getServer().getScheduler().runTaskAsynchronously(
                DiscomcPlugin.getDiscomcPlugin(), () -> broadcastMessage(event));
    }

    public void broadcastMessage(AsyncPlayerChatEvent event){
        WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder();
        String uuidString;
        if (Bukkit.getOnlineMode()) uuidString = event.getPlayer().getUniqueId().toString();
        else {
            uuidString = String.valueOf(DiscomcUtil.getUuidPlayer(event.getPlayer().getName()));
            if (uuidString == null){
                uuidString = "00000000-0000-0000-0000-000000000000"; // zero
            }
        }
        messageBuilder.setUsername(event.getPlayer().getDisplayName())
                .setContent(event.getMessage())
                .setAvatarUrl(String.format("https://crafatar.com/avatars/%s?default=MHF_Steve&overlay", uuidString));
        DiscomcPlugin.getDiscomcPlugin().getMultiChatModule()
                .getWebhookClient().send(messageBuilder.build());
    }

}
