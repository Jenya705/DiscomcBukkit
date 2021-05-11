package com.github.jenya705.command.multi.application;

import com.github.jenya705.command.multi.MultiCommandDescription;
import discord4j.discordjson.json.ApplicationCommandRequest;

public interface MultiApplicationRequestFactory {

    ApplicationCommandRequest create(MultiCommandDescription description);

}
