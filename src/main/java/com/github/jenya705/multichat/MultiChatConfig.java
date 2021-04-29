package com.github.jenya705.multichat;

import com.github.jenya705.DiscomcConfig;
import com.github.jenya705.data.MinecraftConfigData;
import com.github.jenya705.data.SerializedData;
import lombok.Data;

@Data
public class MultiChatConfig implements DiscomcConfig {

    private boolean enabled = true;
    private boolean minecraftToDiscord = true;
    private boolean discordToMinecraft = true;
    private boolean discordAttachments = true;
    private boolean discordMentions = true;
    private boolean mentionColors = true;
    private String attachmentPattern = "&9[{0}] &r";
    private long multiChatChannelID = -1;
    private String discordToMinecraftMessagePattern = "&b[Discord]&r <{0}> {1}";
    private String webhookURL = "";

    @Override
    public void load(SerializedData data) {
        if (data instanceof MinecraftConfigData) {
            MinecraftConfigData minecraftConfigData = (MinecraftConfigData) data;
            setAttachmentPattern(minecraftConfigData.getMessage(
                    "attachmentPattern", getAttachmentPattern(), false));
            setDiscordToMinecraftMessagePattern(minecraftConfigData.getMessage(
                    "discordToMinecraftMessagePattern", getDiscordToMinecraftMessagePattern(), false));
        }
        setEnabled(data.getBoolean("enabled", isEnabled()));
        setMinecraftToDiscord(data.getBoolean("minecraftToDiscord", isMinecraftToDiscord()));
        setDiscordToMinecraft(data.getBoolean("discordToMinecraft", isDiscordToMinecraft()));
        setDiscordAttachments(data.getBoolean("discordAttachments", isDiscordAttachments()));
        setMultiChatChannelID(data.getLong("multiChatChannelID", getMultiChatChannelID()));
        setDiscordMentions(data.getBoolean("discordMentions", isDiscordMentions()));
        setMentionColors(data.getBoolean("mentionColors", isMentionColors()));
        setWebhookURL(data.getString("webhookURL", getWebhookURL()));
    }

    @Override
    public void save(SerializedData data) {
        data.setObject("enabled", isEnabled());
        data.setObject("minecraftToDiscord", isMinecraftToDiscord());
        data.setObject("discordToMinecraft", isDiscordToMinecraft());
        data.setObject("discordAttachments", isDiscordAttachments());
        data.setObject("multiChatChannelID", getMultiChatChannelID());
        data.setObject("discordMentions", isDiscordMentions());
        data.setObject("mentionColors", isMentionColors());
        data.setObject("webhookURL", getWebhookURL());
        if (data instanceof MinecraftConfigData) {
            MinecraftConfigData minecraftConfigData = (MinecraftConfigData) data;
            minecraftConfigData.setMessage("attachmentPattern", getAttachmentPattern(), false);
            minecraftConfigData.setMessage("discordToMinecraftMessagePattern", getDiscordToMinecraftMessagePattern(), false);
        }
    }
}
