package com.github.jenya705.connect;

import com.github.jenya705.Discomc;
import com.github.jenya705.DiscomcDatabaseUser;
import com.github.jenya705.database.DatabaseModule;
import com.github.jenya705.util.EventsUtil;
import com.github.jenya705.util.UsersUtil;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class ConnectMessageCreateHandler implements Consumer<MessageCreateEvent> {

    @Override
    public void accept(MessageCreateEvent messageCreateEvent) {
        Message message = messageCreateEvent.getMessage();
        MessageChannel messageChannel = message.getChannel().block();
        Discomc discomc = Discomc.getPlugin();
        ConnectModule connectModule = discomc.getConnectModule();
        User author = message.getAuthor().get();
        DatabaseModule databaseModule = discomc.getDatabaseModule();
        if (databaseModule.getConfig().isDiscordIDUnique()) {
            Set<DiscomcDatabaseUser> users = UsersUtil.getUsers(author.getId().asLong());
            if (!users.isEmpty()) {
                messageChannel.createMessage(MessageFormat.format(
                        connectModule.getConfig().getAlreadyConnectedDiscord(),
                        author.getMention()
                        ))
                        .block().delete()
                        .block(Duration.ofSeconds(connectModule.getConfig().getDiscordMessageDeleteTime()));
                message.delete().block();
                return;
            }
        }
        int code;
        try {
            code = Integer.parseInt(message.getContent());
        } catch (NumberFormatException e) {
            messageChannel.createMessage(MessageFormat.format(
                    connectModule.getConfig().getCodeNotExist(), author.getMention()))
                    .block().delete()
                    .block(Duration.ofSeconds(connectModule.getConfig().getDiscordMessageDeleteTime()));
            message.delete().block();
            return;
        }
        if (connectModule.getCodes().containsKey(code)) {
            UUID playerUUID = connectModule.getCodes().remove(code);
            AsyncConnectSuccessEvent event = new AsyncConnectSuccessEvent(message, playerUUID, true);
            EventsUtil.callEvent(event);
            if (connectModule.getConfig().isCanEventBeCancelled() && event.isCancelled()) {
                messageChannel.createMessage(MessageFormat.format(
                        connectModule.getConfig().getEventCancelledDiscord(),
                        author.getMention()))
                        .block().delete()
                        .block(Duration.ofSeconds(connectModule.getConfig().getDiscordMessageDeleteTime()));
                return;
            }
            if (connectModule.getConfig().isCanEventChangeVariables()) {
                playerUUID = event.getPlayerUUID();
            }
            databaseModule.getConnector().insert(playerUUID, author.getId().asLong());
            messageChannel.createMessage(MessageFormat.format(
                    connectModule.getConfig().getSuccessfullyConnectedDiscord(),
                    author.getMention()))
                    .block().delete()
                    .block(Duration.ofSeconds(connectModule.getConfig().getDiscordMessageDeleteTime()));
            if (connectModule.getConfig().getConnectedRoleID() != -1) {
                author.asMember(Snowflake.of(discomc.getDiscordModule().getConfig().getMainGuildID()))
                        .block()
                        .addRole(Snowflake.of(connectModule.getConfig().getConnectedRoleID()))
                        .block();
            }
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                player.sendMessage(MessageFormat.format(
                        connectModule.getConfig().getSuccessfullyConnectedMinecraft(),
                        author.getUsername() + "#" + author.getDiscriminator()));
            }
        }
        else {
            messageChannel.createMessage(MessageFormat.format(
                    connectModule.getConfig().getCodeNotExist(),
                    author.getMention()))
                    .block().delete()
                    .delayElement(Duration.ofSeconds(
                            connectModule.getConfig().getDiscordMessageDeleteTime()))
                    .block();
        }
        message.delete().block();
    }
}
