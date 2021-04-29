package com.github.jenya705.connect;

import com.github.jenya705.Discomc;
import com.github.jenya705.DiscomcModule;
import com.github.jenya705.discord.DiscordModule;
import com.github.jenya705.util.ConfigsUtil;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.TextChannel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.PluginCommand;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter(AccessLevel.PROTECTED)
public class ConnectModule implements DiscomcModule {

    private static final Random random = new Random();

    private ConnectConfig config;
    private boolean localEnabled;
    private Map<Integer, UUID> codes;

    @Override
    public void onLoad() {
        setConfig(ConfigsUtil.loadConfig(new ConnectConfig(), "connect"));
        setLocalEnabled(getConfig().isEnabled());
        setCodes(new ConcurrentHashMap<>());
    }

    @Override
    public void onEnable() {
        Discomc discomc = Discomc.getPlugin();
        DiscordModule discordModule = discomc.getDiscordModule();
        boolean createNewChannel = true;
        if (getConfig().getConnectChannelID() != -1) {
            Channel channel = discordModule.getClient().getChannelById(
                    Snowflake.of(getConfig().getConnectChannelID()))
                    .filter(it -> it.getType() == Channel.Type.GUILD_TEXT)
                    .block();
            if (channel == null) {
                discomc.getLogger().warning("[Connect] Connect Channel ID " +
                        "is invalid, creating new channel");
            }
            else {
                createNewChannel = false;
            }
        }
        if (createNewChannel) {
            TextChannel connectTextChannel = discordModule.getMainGuild().createTextChannel(it -> it
                    .setParentId(Snowflake.of(discordModule.getConfig().getMainCategoryID()))
                    .setName("discomc-connect")
                    .setReason("Discomc Connect Channel Creation")).block();
            getConfig().setConnectChannelID(connectTextChannel.getId().asLong());
        }
        PluginCommand connectCommand = discomc.getCommand("connect");
        if (connectCommand != null) {
            ConnectCommand executor = new ConnectCommand();
            connectCommand.setExecutor(executor);
        }
        else {
            discomc.getLogger().warning("[Connect] Connect Command is not defined");
        }
        discordModule.getClient().getEventDispatcher()
                .on(MessageCreateEvent.class)
                .filter(it -> it.getMessage().getChannelId().asLong() == getConfig().getConnectChannelID())
                .filter(it -> it.getMessage().getAuthor().isPresent() && !it.getMessage().getAuthor().get().isBot())
                .subscribe(new ConnectMessageCreateHandler());
        if (getConfig().isKickIfNotConnected()) {
            discomc.getServer().getPluginManager().registerEvents(
                    new ConnectKickHandler(), discomc);
        }
        ConfigsUtil.saveConfig(getConfig(), "connect");
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean isEnabled() {
        return isLocalEnabled();
    }

    @Override
    public void disable() {
        setLocalEnabled(false);
    }

    public int registerCode(UUID uuid) {
        int code;
        do {
            code = random.nextInt(getConfig().getMaxCodeValue());
        } while (codes.containsKey(code));
        getCodes().put(code, uuid);
        final int finalCode = code;
        Discomc.getPlugin().getScheduler().runTaskLaterAsynchronously(() -> {
            UUID currentCodeUUID = getCodes().getOrDefault(finalCode, null);
            if (currentCodeUUID == null || currentCodeUUID.equals(uuid)) return;
            getCodes().remove(finalCode);
        }, getConfig().getCodeRemoveTime());
        return code;
    }

}
