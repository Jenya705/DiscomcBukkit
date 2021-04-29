package com.github.jenya705.console;

import com.github.jenya705.Discomc;
import com.github.jenya705.discord.DiscordModule;
import com.github.jenya705.service.DiscomcService;
import com.github.jenya705.util.MessagesUtil;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.TextChannel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.Deque;

@Getter
@Setter(AccessLevel.PROTECTED)
public class ConsoleDiscordSenderService implements DiscomcService {

    private Deque<String> messagesDeque;

    @Override
    public void setup() {
        Discomc discomc = Discomc.getPlugin();
        DiscordModule discordModule = discomc.getDiscordModule();
        ConsoleModule consoleModule = discomc.getConsoleModule();
        setMessagesDeque(new ArrayDeque<>());
        discomc.getScheduler().runTaskTimerAsynchronously(() -> {
            TextChannel channel = (TextChannel) discordModule.getMainGuild()
                    .getChannelById(Snowflake.of(consoleModule.getConfig().getConsoleChannelID()))
                    .filter(it -> it.getType() == Channel.Type.GUILD_TEXT)
                    .block();
            if (channel == null) {
                discomc.getLogger().warning("[Console] Channel is not exist");
                return;
            }
            StringBuilder currentMessageBuilder = new StringBuilder();
            while (getMessagesDeque().peek() != null) {
                String currentMessageViewing = MessagesUtil.serializePlainMessage(messagesDeque.pop());
                if (currentMessageViewing.length() > 2000) {
                    if (currentMessageBuilder.length() != 0) {
                        channel.createMessage(currentMessageBuilder.toString()).block();
                        currentMessageBuilder = new StringBuilder();
                    }
                    for (int i = 0; i <= currentMessageViewing.length() / 2000; ++i) {
                        channel.createMessage(currentMessageViewing.substring(i*2000, (i+1)*2000)).block();
                    }
                    continue;
                }
                else if (currentMessageViewing.length() + currentMessageBuilder.length() > 1999) {
                    channel.createMessage(currentMessageBuilder.toString()).block();
                    currentMessageBuilder = new StringBuilder();
                }
                currentMessageBuilder.append(currentMessageViewing).append("\n");
            }
            if (currentMessageBuilder.length() != 0) {
                channel.createMessage(currentMessageBuilder.toString()).block();
            }
        }, consoleModule.getConfig().getMessagesSendingTimer());
    }
}
