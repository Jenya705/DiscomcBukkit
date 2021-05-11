package com.github.jenya705.util;

import com.github.jenya705.Discomc;
import com.github.jenya705.DiscomcConfig;
import com.github.jenya705.data.SerializedData;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.io.File;

@UtilityClass
public class ConfigsUtil {

    public static <T extends DiscomcConfig> T loadConfig(T config, String name) {
        Discomc discomc = Discomc.getPlugin();
        SerializedData configDirectory = discomc.getDataConfig().getDirectory(name,
                discomc.getDataFactory().createData());
        config.load(configDirectory);
        config.save(configDirectory);
        discomc.getDataConfig().setObject(name, configDirectory);
        return config;
    }

    public static void saveConfig(DiscomcConfig config, String name){
        Discomc discomc = Discomc.getPlugin();
        SerializedData configDirectory = discomc.getDataConfig().getDirectory(name,
                discomc.getDataFactory().createData());
        config.save(configDirectory);
        discomc.getDataConfig().setObject(name, configDirectory);
    }

    public static String toColorizedMessage(String message){
        return message.replaceAll("&", Character.toString(ChatColor.COLOR_CHAR));
    }

    public static String fromColorizedMessage(String message){
        return message.replaceAll(Character.toString(ChatColor.COLOR_CHAR), "&");
    }

    public static <T extends DiscomcConfig> T loadConfig(T config, SerializedData data) {
        config.load(data);
        config.save(data);
        return config;
    }

    @SneakyThrows
    public static <T extends DiscomcConfig> T loadConfig(T config, File file) {
        Discomc discomc = Discomc.getPlugin();
        if (!file.exists()) {
            file.createNewFile();
        }
        return loadConfig(config, discomc.getDataFactory().createData(file));
    }

}
