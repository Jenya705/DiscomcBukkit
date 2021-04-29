package com.github.jenya705.command;

import discord4j.discordjson.json.ApplicationCommandInteractionData;

public interface SlashDiscordCommand {

    void execute(ApplicationCommandInteractionData data);

}
