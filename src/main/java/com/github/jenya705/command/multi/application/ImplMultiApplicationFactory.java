package com.github.jenya705.command.multi.application;

import com.github.jenya705.command.multi.MultiCommandDescription;
import com.github.jenya705.command.multi.MultiCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;

import java.util.stream.Collectors;

public class ImplMultiApplicationFactory implements MultiApplicationOptionDataFactory, MultiApplicationRequestFactory {

    @Override
    public ApplicationCommandOptionData create(MultiCommandOption option) {
        return ApplicationCommandOptionData.builder()
                .name(option.getName())
                .description(option.getDescription())
                .required(option.isRequired())
                .addAllOptions(option.getOptions()
                        .stream()
                        .map(this::create)
                        .collect(Collectors.toList())
                )
                .addAllChoices(option.getChoices()
                        .stream()
                        .map(it -> ApplicationCommandOptionChoiceData.builder()
                                .name(it.getName())
                                .value(it.getValue())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public ApplicationCommandRequest create(MultiCommandDescription description) {
        return ApplicationCommandRequest.builder()
                .name(description.getName())
                .description(description.getDescription())
                .addAllOptions(description.getOptions()
                        .stream()
                        .map(this::create)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
