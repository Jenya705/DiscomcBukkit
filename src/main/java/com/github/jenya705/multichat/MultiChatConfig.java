package com.github.jenya705.multichat;

import com.github.jenya705.DiscomcConfig;
import com.github.jenya705.data.SerializedData;
import com.github.jenya705.util.ConfigsUtil;
import lombok.Data;

@Data
public class MultiChatConfig implements DiscomcConfig {

    private boolean enabled = true;
    private boolean minecraftToDiscord = true;
    private boolean discordToMinecraft = true;
    private long multiChatChannelID = -1;
    private String discordToMinecraftMessagePattern = "&b[Discord]&r <{0}> {1}";
    private String webhookURL = "";

    @Override
    public void load(SerializedData data) {
        setEnabled(data.getBoolean("enabled", isEnabled()));
        setMinecraftToDiscord(data.getBoolean("minecraftToDiscord", isMinecraftToDiscord()));
        setDiscordToMinecraft(data.getBoolean("discordToMinecraft", isDiscordToMinecraft()));
        setMultiChatChannelID(data.getLong("multiChatChannelID", getMultiChatChannelID()));
        setDiscordToMinecraftMessagePattern(ConfigsUtil.toColorizedMessage(
                data.getString("discordToMinecraftMessagePattern", getDiscordToMinecraftMessagePattern())));
        setWebhookURL(data.getString("webhookURL", getWebhookURL()));
    }

    @Override
    public void save(SerializedData data) {
        data.setObject("enabled", isEnabled());
        data.setObject("minecraftToDiscord", isMinecraftToDiscord());
        data.setObject("discordToMinecraft", isDiscordToMinecraft());
        data.setObject("multiChatChannelID", getMultiChatChannelID());
        data.setObject("discordToMinecraftMessagePattern", ConfigsUtil
                .fromColorizedMessage(getDiscordToMinecraftMessagePattern()));
        data.setObject("webhookURL", getWebhookURL());
    }
}
