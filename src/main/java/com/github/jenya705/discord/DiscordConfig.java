package com.github.jenya705.discord;

import com.github.jenya705.Discomc;
import com.github.jenya705.DiscomcConfig;
import com.github.jenya705.data.MinecraftConfigData;
import com.github.jenya705.data.SerializedData;
import lombok.Data;

@Data
public class DiscordConfig implements DiscomcConfig {

    private String token = "";
    private long mainGuildID = -1;
    private long mainCategoryID = -1;
    private String onlineStatus = "online";
    private String activityType = "playing";
    private String activityName = "Minecraft";
    private String activityURL = "https://www.google.com";

    @Override
    public void load(SerializedData data) {
        setToken(data.getString("token", getToken()));
        setMainGuildID(data.getLong("mainGuildID", getMainGuildID()));
        setMainCategoryID(data.getLong("mainCategoryID", getMainCategoryID()));
        setOnlineStatus(data.getString("onlineStatus", getOnlineStatus()));
        SerializedData activityData = data.getDirectory("activity", Discomc.getPlugin().getDataFactory().createData());
        setActivityType(activityData.getString("type", getActivityType()));
        setActivityName(activityData.getString("name", getActivityName()));
        setActivityURL(activityData.getString("url", getActivityURL()));
    }

    @Override
    public void save(SerializedData  data) {
        data.setObject("token", getToken());
        data.setObject("mainGuildID", getMainGuildID());
        data.setObject("mainCategoryID", getMainCategoryID());
        data.setObject("onlineStatus", getOnlineStatus());
        SerializedData activityData = data.getDirectory("activity", Discomc.getPlugin().getDataFactory().createData());
        activityData.setObject("type", getActivityType());
        activityData.setObject("name", getActivityName());
        activityData.setObject("url", getActivityURL());
        data.setObject("activity", activityData);
    }
}
