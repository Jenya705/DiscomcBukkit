package com.github.jenya705.discord;

import com.github.jenya705.Discomc;
import com.github.jenya705.DiscomcModule;
import com.github.jenya705.util.ConfigsUtil;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.Category;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

@Getter
@Setter(AccessLevel.PROTECTED)
public class DiscordModule implements DiscomcModule {

    private GatewayDiscordClient client;
    private DiscordConfig config;

    @Override
    public void onLoad() {
        setConfig(ConfigsUtil.loadConfig(new DiscordConfig(), "discord"));
        Discomc discomc = Discomc.getPlugin();
        if ("".equals(getConfig().getToken()) || getConfig().getToken() == null){
            discomc.getLogger().severe("[Discord] Disable plugin because token did not set");
            discomc.getPluginLoader().disablePlugin(discomc);
            ConfigsUtil.saveConfig(getConfig(), "discord");
            return;
        }
        discomc.getLogger().info("[Discord] Login");
        setClient(DiscordClientBuilder.create(getConfig().getToken()).build().login().block());
        getClient().updatePresence(createPresenceOnlineStatus(getConfig().getOnlineStatus(),
                createActivity(getConfig().getActivityType(), getConfig().getActivityName(), getConfig().getActivityURL())))
                .doOnError(it -> discomc.getLogger().log(Level.SEVERE, "[Discord] Presence update exception:", it))
                .block();
        Guild mainGuild;
        if (getConfig().getMainGuildID() == -1) {
            List<Guild> guilds = getClient().getGuilds().collectList().block();
            if (guilds.isEmpty()) {
                discomc.getLogger().severe("[Discord] Disable plugin because your bot is not connected to any guild");
                discomc.getPluginLoader().disablePlugin(discomc);
                ConfigsUtil.saveConfig(getConfig(), "discord");
                return;
            }
            else if (guilds.size() == 1){
                mainGuild = guilds.get(0);
                getConfig().setMainGuildID(mainGuild.getId().asLong());
                discomc.getLogger().info(String.format("[Discord] Found guild (name: '%s' id: %s)",
                        mainGuild.getName(), mainGuild.getId().asLong()));
            }
            else {
                discomc.getLogger().severe("[Discord] Disable plugin because bot connected more " +
                        "than 1 guild, set mainGuildID in config");
                discomc.getPluginLoader().disablePlugin(discomc);
                ConfigsUtil.saveConfig(getConfig(), "discord");
                return;
            }
        }
        else {
            mainGuild = getClient().getGuildById(Snowflake.of(getConfig().getMainGuildID())).block();
        }
        if (mainGuild == null){
            discomc.getLogger().severe("[Discord] Disable plugin because main Guild ID is invalid");
            discomc.getPluginLoader().disablePlugin(discomc);
            ConfigsUtil.saveConfig(getConfig(), "discord");
            return;
        }
        boolean createNewDirectory = true;
        if (getConfig().getMainCategoryID() != -1){
            GuildChannel mainCategory = getMainCategory();
            if (mainCategory != null) {
                createNewDirectory = false;
            }
            else {
                discomc.getLogger().warning("[Discord] Main Category ID is invalid, creating new Category");
            }
        }
        if (createNewDirectory) {
            Category mainCategory = mainGuild.createCategory(it -> it
                    .setName("discomc-directory")
                    .setReason("Minecraft plugin directory creation")
                    .setPermissionOverwrites(
                            Collections.singleton(PermissionOverwrite.forRole(
                                    mainGuild.getEveryoneRole().block().getId(),
                                    PermissionSet.none(),
                                    PermissionSet.of(Permission.VIEW_CHANNEL))))
            ).block();
            getConfig().setMainCategoryID(mainCategory.getId().asLong());
        }
        User selfUser = getClient().getSelf().block();
        discomc.getLogger().info(String.format("[Discord] Success! I am %s#%s and I am bot of guild '%s'",
                selfUser.getUsername(), selfUser.getDiscriminator(), mainGuild.getName()));
        ConfigsUtil.saveConfig(getConfig(), "discord");
    }

    @Override
    public void onEnable() {
        /* NOTHING */
    }

    @Override
    public void onDisable() {
        /* NOTHING */
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Category getMainCategory() {
        Channel categoryAsChannel = getClient().getChannelById(
                Snowflake.of(getConfig().getMainCategoryID())).block();
        if (categoryAsChannel instanceof Category) {
            return (Category) categoryAsChannel;
        }
        return null;
    }

    public Guild getMainGuild() {
        return getClient().getGuildById(Snowflake.of(
                getConfig().getMainGuildID())).block();
    }

    public ClientPresence createPresenceOnlineStatus(String onlineStatus, ClientActivity activity) {
        if (onlineStatus.equalsIgnoreCase("doNotDisturb")){
            if (activity != null) return ClientPresence.doNotDisturb(activity);
            return ClientPresence.doNotDisturb();
        }
        else if (onlineStatus.equalsIgnoreCase("idle")){
            if (activity != null) return ClientPresence.idle(activity);
            return ClientPresence.idle();
        }
        else if (onlineStatus.equalsIgnoreCase("invisible")){
            return ClientPresence.invisible();
        }
        else {
            if (activity != null) return ClientPresence.online(activity);
            return ClientPresence.online();
        }
    }

    public ClientActivity createActivity(String type, String name, String url) {
        if (type.equalsIgnoreCase("competing")) {
            return ClientActivity.competing(name);
        }
        else if (type.equalsIgnoreCase("listening")){
            return ClientActivity.listening(name);
        }
        else if (type.equalsIgnoreCase("playing")){
            return ClientActivity.playing(name);
        }
        else if (type.equalsIgnoreCase("streaming")){
            return ClientActivity.streaming(name, url);
        }
        else if (type.equalsIgnoreCase("watching")) {
            return ClientActivity.watching(name);
        }
        return null;
    }

    @Override
    public void disable() {
        Discomc discomc = Discomc.getPlugin();
        discomc.getLogger().severe("[Discord] Discord module disabled, disabling plugin");
        discomc.getServer().getPluginManager().disablePlugin(discomc);
    }
}
