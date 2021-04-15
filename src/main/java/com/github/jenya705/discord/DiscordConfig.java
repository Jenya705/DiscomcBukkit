package com.github.jenya705.discord;

import com.github.jenya705.DiscomcConfig;
import com.github.jenya705.data.SerializedData;
import lombok.Data;

@Data
public class DiscordConfig implements DiscomcConfig {

    private String token = "";
    private long mainGuildID = -1;
    private long mainCategoryID = -1;

    @Override
    public void load(SerializedData data) {
        setToken(data.getString("token"));
        setMainGuildID(data.getLong("mainGuildID"));
        setMainCategoryID(data.getLong("mainCategoryID"));
    }

    @Override
    public void save(SerializedData data) {
        data.setObject("token", getToken());
        data.setObject("mainGuildID", getMainGuildID());
        data.setObject("mainCategoryID", getMainCategoryID());
    }
}
