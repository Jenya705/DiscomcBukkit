package com.github.jenya705.discord;

import com.github.jenya705.Discomc;
import com.github.jenya705.DiscomcModule;
import com.github.jenya705.util.Configs;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.Category;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter(AccessLevel.PROTECTED)
public class DiscordModule implements DiscomcModule {

    private GatewayDiscordClient client;
    private DiscordConfig config;

    @Override
    public void onLoad() {
        setConfig(Configs.loadConfig(new DiscordConfig(), "discord"));
        Discomc discomc = Discomc.getPlugin();
        if ("".equals(getConfig().getToken()) || getConfig().getToken() == null){
            discomc.getLogger().severe("[Discord] Disable plugin because token did not set");
            discomc.getPluginLoader().disablePlugin(discomc);
            return;
        }
        setClient(DiscordClientBuilder.create(getConfig().getToken()).build().login().block());
        Guild mainGuild;
        if (getConfig().getMainGuildID() == -1) {
            List<Guild> guildsData = getClient().getGuilds().collectList().block();
            if (guildsData.isEmpty()) {
                discomc.getLogger().severe("[Discord] Disable plugin because your bot is not connected to any guild");
                discomc.getPluginLoader().disablePlugin(discomc);
                return;
            }
            else if (guildsData.size() == 1){
                mainGuild = guildsData.get(0);
                getConfig().setMainGuildID(mainGuild.getId().asLong());
                discomc.getLogger().info(String.format("[Discord] Found guild (name: %s id: %s)",
                        mainGuild.getId().asLong(), mainGuild.getName()));
            }
            else {
                discomc.getLogger().severe("[Discord] Disable plugin because bot connected more " +
                        "than 1 guild, set mainGuildID in config");
                discomc.getPluginLoader().disablePlugin(discomc);
                return;
            }
        }
        else {
            mainGuild = getClient().getGuildById(Snowflake.of(getConfig().getMainGuildID())).block();
        }
        if (mainGuild == null){
            discomc.getLogger().severe("[Discord] Disable plugin because main Guild ID is invalid");
            discomc.getPluginLoader().disablePlugin(discomc);
            return;
        }
        boolean createNewDirectory = true;
        if (getConfig().getMainCategoryID() != -1){
            GuildChannel mainCategoryAsChannel = mainGuild.getChannelById(
                    Snowflake.of(getConfig().getMainCategoryID())).block();
            if (mainCategoryAsChannel instanceof Category) {
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
        discomc.getLogger().info(String.format("[Discord] Success! I am %s#%s and I am bot of guild %s",
                selfUser.getUsername(), selfUser.getDiscriminator(), mainGuild.getName()));
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

    @Override
    public void disable() {
        Discomc.getPlugin().getLogger().warning("[Discord] Can not disable discord module");
    }
}
