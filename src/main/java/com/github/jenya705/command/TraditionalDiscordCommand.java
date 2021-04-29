package com.github.jenya705.command;

import discord4j.core.event.domain.message.MessageCreateEvent;

public interface TraditionalDiscordCommand {

    void execute(MessageCreateEvent event);

}
