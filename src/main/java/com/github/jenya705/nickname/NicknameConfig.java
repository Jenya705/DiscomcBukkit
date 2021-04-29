package com.github.jenya705.nickname;

import com.github.jenya705.DiscomcConfig;
import com.github.jenya705.data.MinecraftConfigData;
import com.github.jenya705.data.SerializedData;
import lombok.Data;

@Data
public class NicknameConfig implements DiscomcConfig {

    private boolean enabled = true;
    private String nicknamePattern = "{0} | {1}";

    @Override
    public void load(SerializedData data) {
        if (data instanceof MinecraftConfigData) {
            MinecraftConfigData minecraftConfigData = (MinecraftConfigData) data;
            setNicknamePattern(minecraftConfigData.getMessage("nicknamePattern", getNicknamePattern(), false));
        }
        setEnabled(data.getBoolean("enabled", isEnabled()));
    }

    @Override
    public void save(SerializedData data) {
        data.setObject("enabled", isEnabled());
        if (data instanceof MinecraftConfigData) {
            MinecraftConfigData minecraftConfigData = (MinecraftConfigData) data;
            minecraftConfigData.setMessage("nicknamePattern", getNicknamePattern(), false);
        }
    }
}
