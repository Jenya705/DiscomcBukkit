package com.github.jenya705.connect;

import com.github.jenya705.Discomc;
import com.github.jenya705.DiscomcModule;
import com.github.jenya705.discord.DiscordModule;
import com.github.jenya705.util.ConfigsUtil;
import com.github.jenya705.util.EventsUtil;
import com.github.jenya705.util.UsersUtil;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.TextChannel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.time.Duration;
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
            Channel channel = discordModule.getClient().getChannelById(Snowflake.of(getConfig().getConnectChannelID())).filter(it -> it.getType() == Channel.Type.GUILD_TEXT).block();
            if (channel == null) {
                discomc.getLogger().warning("[Connect] Connect Channel ID is invalid, creating new channel");
            } else {
                createNewChannel = false;
            }
        }
        if (createNewChannel) {
            TextChannel connectTextChannel = discordModule.getMainGuild().createTextChannel(it -> it.setParentId(Snowflake.of(discordModule.getConfig().getMainCategoryID())).setName("discomc-connect").setReason("Discomc Connect Channel Creation")).block();
            getConfig().setConnectChannelID(connectTextChannel.getId().asLong());
        }
        PluginCommand connectCommand = discomc.getCommand("connect");
        if (connectCommand != null) {
            ConnectCommand executor = new ConnectCommand();
            connectCommand.setExecutor(executor);
        } else {
            discomc.getLogger().warning("[Connect] Connect Command is not defined");
        }
        discordModule.getClient().getEventDispatcher().on(MessageCreateEvent.class)
                .filter(it -> it.getMessage().getChannelId().asLong() == getConfig().getConnectChannelID())
                .filter(it -> it.getMessage().getAuthor().isPresent() && !it.getMessage().getAuthor().get().isBot())
                .flatMap(event -> event.getMessage().getChannel().flatMap(messageChannel -> {
                    User author = event.getMessage().getAuthor().get();
                    if (discomc.getDatabaseModule().getConfig().isDiscordIDUnique()) {
                        if (!UsersUtil.getUsers(author.getId().asLong()).isEmpty()) {
                            messageChannel.createMessage(MessageFormat.format(
                                            discomc.getConnectModule().getConfig().getAlreadyConnectedDiscord(),
                                            author.getMention()
                                    ))
                                    .delayElement(Duration.ofMillis(discomc.getConnectModule().getConfig().getDiscordMessageDeleteTime()))
                                    .flatMap(Message::delete)
                                    .subscribe();
                            return event.getMessage().delete();
                        }
                    }

                    int code;
                    try {
                        code = Integer.parseInt(event.getMessage().getContent());
                    } catch (NumberFormatException e) {
                        messageChannel.createMessage(MessageFormat.format(
                                        discomc.getConnectModule().getConfig().getCodeNotExist(),
                                        author.getMention()
                                ))
                                .delayElement(Duration.ofMillis(discomc.getConnectModule().getConfig().getDiscordMessageDeleteTime()))
                                .flatMap(Message::delete)
                                .subscribe();
                        return event.getMessage().delete();
                    }
                    if (discomc.getConnectModule().getCodes().containsKey(code)) {
                        UUID playerUUID = discomc.getConnectModule().getCodes().remove(code);
                        AsyncConnectSuccessEvent acsevent = new AsyncConnectSuccessEvent(event.getMessage(), playerUUID, true);
                        EventsUtil.callEvent(acsevent);
                        if (discomc.getConnectModule().getConfig().isCanEventBeCancelled() && acsevent.isCancelled()) {
                            messageChannel.createMessage(MessageFormat.format(
                                            discomc.getConnectModule().getConfig().getEventCancelledDiscord(),
                                            author.getMention()))
                                    .delayElement(Duration.ofMillis(discomc.getConnectModule().getConfig().getDiscordMessageDeleteTime()))
                                    .flatMap(Message::delete)
                                    .subscribe();
                            return Mono.empty();
                        }
                        if (discomc.getConnectModule().getConfig().isCanEventChangeVariables()) {
                            playerUUID = acsevent.getPlayerUUID();
                        }
                        discomc.getDatabaseModule().getConnector().insert(playerUUID, author.getId().asLong());
                        messageChannel.createMessage(MessageFormat.format(
                                        discomc.getConnectModule().getConfig().getSuccessfullyConnectedDiscord(),
                                        author.getMention()))
                                .delayElement(Duration.ofMillis(discomc.getConnectModule().getConfig().getDiscordMessageDeleteTime()))
                                .flatMap(Message::delete)
                                .subscribe();
                        if (discomc.getConnectModule().getConfig().getConnectedRoleID() != -1) {
                            author.asMember(Snowflake.of(discomc.getDiscordModule().getConfig().getMainGuildID()))
                                    .flatMap(v -> v.addRole(Snowflake.of(discomc.getConnectModule().getConfig().getConnectedRoleID())))
                                    .subscribe();
                        }
                        Player player = Bukkit.getPlayer(playerUUID);
                        if (player != null) {
                            player.sendMessage(MessageFormat.format(
                                    discomc.getConnectModule().getConfig().getSuccessfullyConnectedMinecraft(),
                                    author.getUsername() + "#" + author.getDiscriminator()));
                        }
                    } else {
                        messageChannel.createMessage(MessageFormat.format(
                                        discomc.getConnectModule().getConfig().getCodeNotExist(),
                                        author.getMention()))
                                .delayElement(Duration.ofMillis(
                                        discomc.getConnectModule().getConfig().getDiscordMessageDeleteTime()))
                                .flatMap(Message::delete)
                                .subscribe();
                    }
                    return event.getMessage().delete();
                })).subscribe();
        if (getConfig().isKickIfNotConnected()) {
            discomc.getServer().getPluginManager().registerEvents(new ConnectKickHandler(), discomc);
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
