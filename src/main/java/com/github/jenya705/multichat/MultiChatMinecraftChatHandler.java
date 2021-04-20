package com.github.jenya705.multichat;

import com.github.jenya705.Discomc;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Level;

public class MultiChatMinecraftChatHandler implements Listener {

    @EventHandler
    public void playerChat(AsyncChatEvent event) {
        Discomc discomc = Discomc.getPlugin();
        MultiChatModule multiChatModule = discomc.getMultiChatModule();
        discomc.getScheduler().runTaskAsynchronously(() ->
            multiChatModule.getWebhook().execute(it -> it
                    .setUsername(PlainComponentSerializer.plain().serialize(
                            event.getPlayer().displayName()))
                    .setAvatarUrl(discomc.getHeadURLFactory().getHeadURL(Bukkit.getOnlineMode() ?
                            event.getPlayer().getUniqueId() :
                            discomc.getUuidFactory().getUUID(event.getPlayer().getName())))
                    .setContent(PlainComponentSerializer.plain().serialize(event.message()))
            ).doOnError(e -> discomc.getLogger().log(Level.SEVERE, "[Discord4J] Webhook Exception:", e)).block()
        );
    }

}
