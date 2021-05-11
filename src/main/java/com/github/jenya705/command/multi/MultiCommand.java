package com.github.jenya705.command.multi;

import com.github.jenya705.command.parser.CommandData;

public interface MultiCommand {

    String execute(CommandData data, MultiCommandSenderType senderType);

    boolean isAsync();

    boolean isMinecraft();

    boolean isDiscordTraditional();

    boolean isDiscordApplication();

}
