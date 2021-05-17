package com.github.jenya705.command.definition;

import com.github.jenya705.command.MinecraftCommand;
import com.github.jenya705.command.SlashDiscordCommand;
import com.github.jenya705.command.TraditionalDiscordCommand;
import lombok.*;

import java.util.List;

@Getter
@Setter(AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommandDefinitionOptionImpl implements CommandDefinitionOption {

    private String name;
    private String description;
    private String permission;
    private boolean sub;
    private MinecraftCommand minecraftExecutor;
    private SlashDiscordCommand slashDiscordExecutor;
    private TraditionalDiscordCommand traditionalDiscordExecutor;
    private List<CommandDefinitionOption> options;
    private List<CommandDefinitionChoose> chooses;
    private CommandDefinitionValueType valueType;

    public static class CommandDefinitionOptionImplBuilder {
        public CommandDefinitionOptionImplBuilder executor(Object obj) {
            if (obj instanceof MinecraftCommand) minecraftExecutor = (MinecraftCommand) obj;
            if (obj instanceof SlashDiscordCommand) slashDiscordExecutor = (SlashDiscordCommand) obj;
            if (obj instanceof TraditionalDiscordCommand) traditionalDiscordExecutor = (TraditionalDiscordCommand) obj;
            return this;
        }
        public CommandDefinitionOptionImplBuilder sub() {
            this.sub = true;
            return this;
        }
        public CommandDefinitionOptionImplBuilder value() {
            this.sub = false;
            return this;
        }
    }

}
