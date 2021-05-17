package com.github.jenya705.command;

import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;

public interface SlashDiscordCommand {

    String execute(ApplicationCommandInteraction data, ApplicationCommandInteractionOption option);

}
