package com.github.jenya705;

import com.github.jenya705.data.MinecraftConfigData;
import com.github.jenya705.data.SerializedData;
import lombok.Data;

@Data
public class DefaultConfig implements DiscomcConfig {

    private String prefix = "&8[&bDisco&amc&8] &r";

    @Override
    public void load(SerializedData data) {
        if (data instanceof MinecraftConfigData) {
            MinecraftConfigData minecraftConfigData = (MinecraftConfigData) data;
            setPrefix(minecraftConfigData.getMessage("prefix", getPrefix(), false));
        }
    }

    @Override
    public void save(SerializedData data) {
        if (data instanceof MinecraftConfigData) {
            MinecraftConfigData minecraftConfigData = (MinecraftConfigData) data;
            minecraftConfigData.setMessage("prefix", getPrefix(), false);
        }
    }
}
