package com.github.jenya705.command.multi.application;

import com.github.jenya705.command.multi.MultiCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;

public interface MultiApplicationOptionDataFactory {

    ApplicationCommandOptionData create(MultiCommandOption option);

}
