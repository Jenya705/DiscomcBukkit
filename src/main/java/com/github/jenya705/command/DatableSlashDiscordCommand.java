package com.github.jenya705.command;

import com.github.jenya705.command.parser.CommandData;
import discord4j.core.event.domain.InteractionCreateEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;

public interface DatableSlashDiscordCommand {

    /**
     * @param event Event
     * @param data Data
     * @param option Data was created by dataFrom
     * @return reply
     */
    String execute(InteractionCreateEvent event, CommandData data, ApplicationCommandInteractionOption option);

}
