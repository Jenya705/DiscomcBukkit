package com.github.jenya705.multichat;

import com.github.jenya705.DiscomcConfig;
import com.github.jenya705.data.MinecraftConfigData;
import com.github.jenya705.data.SerializedData;
import com.github.jenya705.util.ConfigsUtil;
import lombok.Data;

@Data
public class MultiChatConfig implements DiscomcConfig {

    private boolean enabled = true;
    private boolean minecraftToDiscord = true;
    private boolean discordToMinecraft = true;
    private boolean discordAttachments = true;
    private String attachmentPattern = "&9[{0}] &r";
    private long multiChatChannelID = -1;
    private String discordToMinecraftMessagePattern = "&b[Discord]&r <{0}> {1}";
    private String webhookURL = "";

    @Override
    public void load(SerializedData data) {
        if (data instanceof MinecraftConfigData) {
            MinecraftConfigData minecraftConfigData = (MinecraftConfigData) data;
            setAttachmentPattern(minecraftConfigData.getMessage(
                    "attachmentPattern", getAttachmentPattern(), true));
            setDiscordToMinecraftMessagePattern(minecraftConfigData.getMessage(
                    "discordToMinecraftMessagePattern", getDiscordToMinecraftMessagePattern(), true));
        }
        setEnabled(data.getBoolean("enabled", isEnabled()));
        setMinecraftToDiscord(data.getBoolean("minecraftToDiscord", isMinecraftToDiscord()));
        setDiscordToMinecraft(data.getBoolean("discordToMinecraft", isDiscordToMinecraft()));
        setDiscordAttachments(data.getBoolean("discordAttachments", isDiscordAttachments()));
        setMultiChatChannelID(data.getLong("multiChatChannelID", getMultiChatChannelID()));
        setWebhookURL(data.getString("webhookURL", getWebhookURL()));
    }

    @Override
    public void save(SerializedData data) {
        data.setObject("enabled", isEnabled());
        data.setObject("minecraftToDiscord", isMinecraftToDiscord());
        data.setObject("discordToMinecraft", isDiscordToMinecraft());
        data.setObject("discordAttachments", isDiscordAttachments());
        data.setObject("multiChatChannelID", getMultiChatChannelID());
        data.setObject("webhookURL", getWebhookURL());
        if (data instanceof MinecraftConfigData) {
            MinecraftConfigData minecraftConfigData = (MinecraftConfigData) data;
            minecraftConfigData.setMessage("attachmentPattern", getAttachmentPattern(), true);
            minecraftConfigData.setMessage("discordToMinecraftMessagePattern", getDiscordToMinecraftMessagePattern(), true);
        }
    }
}
