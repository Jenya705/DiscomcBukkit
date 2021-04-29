package com.github.jenya705.console;

import com.github.jenya705.Discomc;
import com.github.jenya705.DiscomcModule;
import com.github.jenya705.discord.DiscordModule;
import com.github.jenya705.util.ConfigsUtil;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.TextChannel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PROTECTED)
public class ConsoleModule implements DiscomcModule {

    private ConsoleConfig config;
    private boolean localEnabled;

    private ConsoleDiscordSenderService consoleSenderService;

    @Override
    public void onLoad() {
        setConfig(ConfigsUtil.loadConfig(new ConsoleConfig(), "console"));
        setLocalEnabled(getConfig().isEnabled());
    }

    @Override
    public void onEnable() {
        Discomc discomc = Discomc.getPlugin();
        DiscordModule discordModule = discomc.getDiscordModule();
        boolean createChannel = true;
        if (getConfig().getConsoleChannelID() != -1) {
            GuildChannel consoleChannel = discordModule.getMainGuild()
                    .getChannelById(Snowflake.of(getConfig().getConsoleChannelID()))
                    .filter(it -> it.getType() == Channel.Type.GUILD_TEXT)
                    .block();
            if (consoleChannel == null) {
                discomc.getLogger().warning("[Console] Console channel id is invalid, creating new channel");
            }
            else {
                createChannel = false;
            }
        }
        if (createChannel) {
            TextChannel channel = discordModule.getMainGuild().createTextChannel(it -> it
                    .setName("discomc-console")
                    .setReason("Discomc Console Channel Creation")
                    .setParentId(Snowflake.of(discordModule.getConfig().getMainCategoryID())))
                    .block();
            if (channel == null) {
                discomc.getLogger().warning("[Console] Console channel creation failed, disabling module");
                setLocalEnabled(false);
                return;
            }
            getConfig().setConsoleChannelID(channel.getId().asLong());
        }
        ConfigsUtil.saveConfig(getConfig(), "console");
        setConsoleSenderService(discomc.addService(new ConsoleDiscordSenderService()));
        discomc.getCoreLogger().addAppender(new ConsoleAppender());
        discordModule.getClient().on(MessageCreateEvent.class)
                .filter(it -> it.getMessage().getChannelId().asLong() == getConfig().getConsoleChannelID())
                .filter(it -> it.getMessage().getAuthor().isPresent() && !it.getMessage().getAuthor().get().isBot())
                .subscribe(new ConsoleMessageCreateHandler());
    }

    @Override
    public void onDisable() {
        /* NOTHING */
    }

    @Override
    public boolean isEnabled() {
        return isLocalEnabled();
    }

    @Override
    public void disable() {
        setLocalEnabled(false);
    }
}
