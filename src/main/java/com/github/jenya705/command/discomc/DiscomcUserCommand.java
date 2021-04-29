package com.github.jenya705.command.discomc;

import com.github.jenya705.command.MinecraftCommand;
import com.github.jenya705.command.SlashDiscordCommand;
import com.github.jenya705.command.TraditionalDiscordCommand;
import com.github.jenya705.util.ArrayIterator;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.discordjson.json.ApplicationCommandInteractionData;
import org.bukkit.command.CommandSender;

import java.util.List;

public class DiscomcUserCommand implements MinecraftCommand, SlashDiscordCommand, TraditionalDiscordCommand {

    @Override
    public boolean execute(CommandSender commandSender, ArrayIterator<String> args) {
        return false;
    }

    @Override
    public List<String> tab(CommandSender commandSender, ArrayIterator<String> args) {
        return null;
    }

    @Override
    public void execute(ApplicationCommandInteractionData data) {

    }

    @Override
    public void execute(MessageCreateEvent event) {

    }
}
