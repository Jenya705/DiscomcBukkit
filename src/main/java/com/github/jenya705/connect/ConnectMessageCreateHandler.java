package com.github.jenya705.connect;

import com.github.jenya705.Discomc;
import com.github.jenya705.database.DatabaseModule;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.time.Duration;
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
        int code;
        try {
            code = Integer.parseInt(message.getContent());
        } catch (NumberFormatException e) {
            messageChannel.createMessage(MessageFormat.format(
                    connectModule.getConfig().getCodeNotExist(), author.getMention()))
                    .block().delete()
                    .delayElement(Duration.ofSeconds(
                            connectModule.getConfig().getDiscordMessageDeleteTime()))
                    .block();
            message.delete().block();
            return;
        }
        if (connectModule.getCodes().containsKey(code)) {
            UUID playerUUID = connectModule.getCodes().remove(code);
            DatabaseModule databaseModule = discomc.getDatabaseModule();
            databaseModule.getConnector().insert(playerUUID, author.getId().asLong());
            messageChannel.createMessage(MessageFormat.format(
                    connectModule.getConfig().getSuccessfullyConnectedDiscord(),
                    author.getMention()))
                    .block().delete()
                    .delayElement(Duration.ofSeconds(
                            connectModule.getConfig().getDiscordMessageDeleteTime()))
                    .block();
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
