package com.github.jenya705.command.definition;

import com.github.jenya705.command.MinecraftCommand;
import com.github.jenya705.command.SlashDiscordCommand;
import com.github.jenya705.command.TraditionalDiscordCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CommandDefinitionOption {

    static CommandDefinitionOptionImpl.CommandDefinitionOptionImplBuilder builder() {
        return CommandDefinitionOptionImpl.builder();
    }

    @NotNull String getName();

    @NotNull String getDescription();

    /**
     * @return minecraft permission need to enter this option
     */
    String getPermission();

    boolean isSub();

    CommandDefinitionValueType getValueType();

    MinecraftCommand getMinecraftExecutor();

    TraditionalDiscordCommand getTraditionalDiscordExecutor();

    SlashDiscordCommand getSlashDiscordExecutor();

    List<CommandDefinitionChoose> getChooses();

    List<CommandDefinitionOption> getOptions();

}
