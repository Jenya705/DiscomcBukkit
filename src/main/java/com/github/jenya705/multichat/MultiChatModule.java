package com.github.jenya705.multichat;

import com.github.jenya705.Discomc;
import com.github.jenya705.DiscomcModule;
import com.github.jenya705.discord.DiscordModule;
import com.github.jenya705.util.ConfigsUtil;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Webhook;
import discord4j.core.object.entity.channel.Category;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.discordjson.json.WebhookData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.logging.Level;

@Getter
@Setter(AccessLevel.PROTECTED)
public class MultiChatModule implements DiscomcModule {

    private MultiChatConfig config;
    private boolean localEnabled;
    private Webhook webhook;

    @Override
    public void onLoad() {
        setConfig(ConfigsUtil.loadConfig(new MultiChatConfig(), "multiChat"));
        setLocalEnabled(getConfig().isEnabled());
        if (!isLocalEnabled()) return;
        Discomc discomc = Discomc.getPlugin();
        if (!getConfig().isDiscordToMinecraft() && !getConfig().isMinecraftToDiscord()) {
            discomc.getLogger().warning("[MultiChat] MultiChat disabled because " +
                    "discordToMinecraft and minecraftToDiscord are false");
            setLocalEnabled(false);
        }
    }

    @Override
    public void onEnable() {
        Discomc discomc = Discomc.getPlugin();
        DiscordModule discordModule = discomc.getDiscordModule();
        Guild mainGuild = discordModule.getMainGuild();
        Category mainCategory = discordModule.getMainCategory();
        boolean createMultiChatChannel = true;
        if (getConfig().getMultiChatChannelID() != -1) {
            GuildChannel multiChatChannel = mainGuild.getChannelById(
                    Snowflake.of(getConfig().getMultiChatChannelID()))
                    .block();
            if (multiChatChannel == null) {
                discomc.getLogger().warning("[MultiChat] Multi Chat Channel ID " +
                        "is not valid, creating new Multi Chat Channel");
            }
            else {
                createMultiChatChannel = false;
            }
        }
        if (createMultiChatChannel) {
            TextChannel multiChatChannel = mainGuild.createTextChannel(it -> it
                    .setParentId(mainCategory.getId())
                    .setName("discomc-multichat")
                    .setReason("Discomc MultiChat channel creation"))
                    .block();
            getConfig().setMultiChatChannelID(multiChatChannel.getId().asLong());
        }
        if (getConfig().isDiscordToMinecraft()) {
            discordModule.getClient().on(MessageCreateEvent.class)
                    .filter(it -> it.getMessage().getChannelId().asLong() == getConfig().getMultiChatChannelID()
                            && getConfig().isDiscordToMinecraft())
                    .subscribe(new MultiChatMessageCreateHandler(), error ->
                            discomc.getLogger().log(Level.SEVERE,
                                    "[Discord4J] MessageCreateEvent exception:", error));
        }
        if (getConfig().isMinecraftToDiscord()) {
            boolean localMinecraftToDiscord = true;
            if (getConfig().getWebhookURL() == null || getConfig().getWebhookURL().equals("")) {
                if (mainGuild.getApplicationId().isPresent()) {
                    setWebhook(new Webhook(discordModule.getClient(), WebhookData.builder()
                            .applicationId(mainGuild.getApplicationId().get().asLong())
                            .channelId(getConfig().getMultiChatChannelID())
                            .name("Discomc-MultiChat-Webhook")
                            .type(1).build()));
                    if (getWebhook().getToken().isPresent()) {
                        getConfig().setWebhookURL(
                                getWebhook().getId().asString() + "/" +
                                getWebhook().getToken().get());
                    }
                    else {
                        discomc.getLogger().warning("[MultiChat] Token is not present, set webhook url by yourself");
                        localMinecraftToDiscord = false;
                    }
                }
                else {
                    discomc.getLogger().warning("[MultiChat] ApplicationID is not present, set webhook url by yourself");
                    localMinecraftToDiscord = false;
                }
            }
            else {
                String[] splitWebhookURL = getConfig().getWebhookURL().split("/");
                if (splitWebhookURL.length >= 2) {
                    setWebhook(discordModule.getClient().getWebhookByIdWithToken(
                            Snowflake.of(splitWebhookURL[splitWebhookURL.length - 2]),
                            splitWebhookURL[splitWebhookURL.length - 1]).block());
                }
                if (getWebhook() == null){
                    discomc.getLogger().warning("[MultiChat] WebhookURL is wrong");
                    localMinecraftToDiscord = false;
                }
            }
            if (localMinecraftToDiscord) {
                discomc.getServer().getPluginManager().registerEvents(
                        new MultiChatMinecraftChatHandler(), discomc);
            }
        }
        discomc.getLogger().info(String.format("[MultiChat] Chat: Discord %s-%s Minecraft",
                getConfig().isMinecraftToDiscord() ? "<" : "",
                getConfig().isDiscordToMinecraft() ? ">" : ""));
        ConfigsUtil.saveConfig(getConfig(), "multiChat");
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
