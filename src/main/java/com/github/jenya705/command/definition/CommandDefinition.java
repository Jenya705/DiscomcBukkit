package com.github.jenya705.command.definition;

import com.github.jenya705.command.MinecraftCommand;
import com.github.jenya705.command.SlashDiscordCommand;
import com.github.jenya705.command.TraditionalDiscordCommand;

import java.util.List;

public interface CommandDefinition {

    static CommandDefinitionImpl.CommandDefinitionImplBuilder builder() {
        return CommandDefinitionImpl.builder();
    }

    String getName();

    String getDescription();

    /**
     * @return minecraft permission need to execute this command
     */
    String getPermission();

    boolean isSub();

    MinecraftCommand getMinecraftExecutor();

    TraditionalDiscordCommand getTraditionalDiscordExecutor();

    SlashDiscordCommand getSlashDiscordExecutor();

    List<CommandDefinitionOption> getOptions();

}
