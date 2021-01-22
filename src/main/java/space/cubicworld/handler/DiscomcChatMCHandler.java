package space.cubicworld.handler;

import ch.jalu.configme.SettingsManager;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import space.cubicworld.DiscomcConfiguration;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscordManager;

public class DiscomcChatMCHandler implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMessageReceived(AsyncPlayerChatEvent event){

        (new SendMessageRunnable(event.getPlayer(), event.getMessage()))
                .runTaskAsynchronously(DiscomcPlugin.getInstance());

    }

    public static void sendMessage(Player player, String message){

        DiscomcPlugin discomcPlugin = DiscomcPlugin.getInstance();
        SettingsManager discomcConfiguration = discomcPlugin.getDiscomcConfig();
        DiscordManager discordManager = discomcPlugin.getDiscordManager();

        String playerUUID;

        if (discomcConfiguration.getProperty(DiscomcConfiguration.PREMIUM_UUIDS_GET)) playerUUID = discomcPlugin.getMojangApi().getPremiumUUID(player.getName());
        else playerUUID = player.getUniqueId().toString();

        if (playerUUID == null) playerUUID = "b31f5079-c09d-4979-b02e-2396e5fd9afb";

        WebhookMessage webhookMessage = new WebhookMessageBuilder()
                .setAvatarUrl("https://crafatar.com/avatars/" + playerUUID + "?overlay=true")
                .setUsername(player.getName())
                .setContent(message)
                .build();
        discordManager.getWebhookClient().send(webhookMessage);

    }

}
