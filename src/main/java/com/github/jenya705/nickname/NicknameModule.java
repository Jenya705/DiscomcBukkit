package com.github.jenya705.nickname;

import com.github.jenya705.Discomc;
import com.github.jenya705.DiscomcModule;
import com.github.jenya705.discord.DiscordModule;
import com.github.jenya705.util.ConfigsUtil;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.text.MessageFormat;

@Getter
@Setter(AccessLevel.PROTECTED)
public class NicknameModule implements DiscomcModule {

    private NicknameConfig config;
    private boolean localEnabled;

    @Override
    public void onLoad() {
        setConfig(ConfigsUtil.loadConfig(new NicknameConfig(), "nickname"));
        setLocalEnabled(getConfig().isEnabled());
    }

    @Override
    public void onEnable() {
        Discomc discomc = Discomc.getPlugin();
        discomc.getServer().getPluginManager().registerEvents(
                new NicknameConnectListener(), discomc);
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

    public void updateNickname(Snowflake userID, String nickname) {
        Discomc discomc = Discomc.getPlugin();
        DiscordModule discordModule = discomc.getDiscordModule();
        Member member = discordModule.getMainGuild().getMemberById(userID).block();
        String newNickname = MessageFormat.format(getConfig().getNicknamePattern(),
                nickname, member.getUsername());
        if (!member.getDisplayName().equals(newNickname)
                && !member.isHigher(discordModule.getClient().getSelfId()).block()) {
            member.edit(it -> it.setNickname(newNickname)).block();
        }
    }

}
