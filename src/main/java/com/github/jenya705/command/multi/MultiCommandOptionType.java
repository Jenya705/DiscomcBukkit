package com.github.jenya705.command.multi;

import com.github.jenya705.data.types.DiscordValueType;
import com.github.jenya705.data.types.PrimitiveValueType;
import com.github.jenya705.data.types.ValueType;
import discord4j.rest.util.ApplicationCommandOptionType;
import lombok.Getter;

@Getter
public enum MultiCommandOptionType {

    SUB_COMMAND(ApplicationCommandOptionType.SUB_COMMAND, null),
    SUB_COMMAND_GROUP(ApplicationCommandOptionType.SUB_COMMAND_GROUP, null),
    STRING(ApplicationCommandOptionType.STRING, PrimitiveValueType.STRING),
    DOUBLE(ApplicationCommandOptionType.STRING, PrimitiveValueType.DOUBLE),
    INTEGER(ApplicationCommandOptionType.INTEGER, PrimitiveValueType.LONG),
    BOOLEAN(ApplicationCommandOptionType.BOOLEAN, PrimitiveValueType.BOOLEAN),
    USER(ApplicationCommandOptionType.USER, DiscordValueType.SNOWFLAKE_USER),
    CHANNEL(ApplicationCommandOptionType.CHANNEL, DiscordValueType.SNOWFLAKE_CHANNEL),
    ROLE(ApplicationCommandOptionType.ROLE, DiscordValueType.SNOWFLAKE_ROLE),
    MENTIONABLE(9, DiscordValueType.SNOWFLAKE_MENTIONABLE)
    ;

    private final int discordID;
    private final ValueType local;
    MultiCommandOptionType(int discordID, ValueType local) {
        this.discordID = discordID;
        this.local = local;
    }
    MultiCommandOptionType(ApplicationCommandOptionType discord, ValueType local) {
        this(discord.getValue(), local);
    }

}
