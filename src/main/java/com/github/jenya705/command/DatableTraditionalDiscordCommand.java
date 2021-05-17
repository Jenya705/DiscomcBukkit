package com.github.jenya705.command;

import com.github.jenya705.command.parser.CommandData;
import com.github.jenya705.util.ArrayIterator;
import discord4j.core.event.domain.message.MessageCreateEvent;

public interface DatableTraditionalDiscordCommand {

    void execute(MessageCreateEvent event, ArrayIterator<String> args, CommandData data);

}
