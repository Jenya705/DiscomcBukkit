package com.github.jenya705.console;

import com.github.jenya705.Discomc;
import com.github.jenya705.service.AsyncCommandsService;
import com.github.jenya705.util.Pair;
import discord4j.core.event.domain.message.MessageCreateEvent;

import java.util.function.Consumer;

public class ConsoleMessageCreateHandler implements Consumer<MessageCreateEvent> {

    @Override
    public void accept(MessageCreateEvent messageCreateEvent) {
        AsyncCommandsService.addCommand(messageCreateEvent.getMessage().getContent());
    }
}
