package com.github.jenya705.command;

import discord4j.core.object.command.ApplicationCommandInteraction;

public interface SlashDiscordCommand {

    String execute(ApplicationCommandInteraction data);

}
