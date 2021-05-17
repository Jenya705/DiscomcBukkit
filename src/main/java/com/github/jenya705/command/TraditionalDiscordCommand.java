package com.github.jenya705.command;

import com.github.jenya705.util.ArrayIterator;
import discord4j.core.event.domain.message.MessageCreateEvent;

public interface TraditionalDiscordCommand {

    void execute(MessageCreateEvent event, ArrayIterator<String> args);

}
