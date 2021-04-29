package com.github.jenya705.connect;

import com.github.jenya705.Discomc;
import com.github.jenya705.DiscomcDatabaseUser;
import com.github.jenya705.discord.DiscordModule;
import com.github.jenya705.util.UsersUtil;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.TextChannel;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicReference;

public class ConnectKickHandler implements Listener {

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Discomc discomc = Discomc.getPlugin();
        ConnectModule connectModule = discomc.getConnectModule();
        if (!connectModule.getConfig().isKickIfNotConnected()) return;
        DiscomcDatabaseUser discomcDatabaseUser = UsersUtil.getUser(event.getPlayer().getUniqueId());
        if (discomcDatabaseUser != null) return;
        AtomicReference<String> channelName = new AtomicReference<>("INVALID");
        DiscordModule discordModule = discomc.getDiscordModule();
        discordModule.getClient().getChannelById(Snowflake.of(
                connectModule.getConfig().getConnectChannelID()))
                .filter(it -> it.getType() == Channel.Type.GUILD_TEXT)
                .blockOptional()
                .ifPresent(it -> channelName.set(((TextChannel) it).getName()));
        event.getPlayer().kick(Component.text(MessageFormat.format(
                connectModule.getConfig().getKickMessage(),
                channelName.get(), Integer.toString(
                        connectModule.registerCode(event.getPlayer().getUniqueId()))
                )));
    }

}
