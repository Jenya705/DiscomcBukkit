package com.github.jenya705.util;

import com.github.jenya705.Discomc;
import com.github.jenya705.DiscomcConfig;
import com.github.jenya705.data.SerializedData;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Configs {

    public static <T extends DiscomcConfig> T loadConfig(T config, String name) {
        Discomc discomc = Discomc.getPlugin();
        SerializedData configDirectory = discomc.getDataConfig().getDirectory(name);
        config.load(configDirectory);
        config.save(configDirectory);
        return config;
    }

    public static void saveConfig(DiscomcConfig config, String name){
        Discomc discomc = Discomc.getPlugin();
        SerializedData configDirectory = discomc.getDataConfig().getDirectory(name);
        config.save(configDirectory);
    }

}
