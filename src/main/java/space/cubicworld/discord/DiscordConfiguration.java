package space.cubicworld.discord;

import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;
import space.cubicworld.DiscomcConfig;

import java.util.Map;

@Data
public class DiscordConfiguration implements DiscomcConfig {

    private String token = "token";
    private long mainServerID = 0;

    public DiscordConfiguration(FileConfiguration config){
        load(config);
    }

    @Override
    public void load(FileConfiguration config) {
        setToken(config.getString("discord.botToken", getToken()));
        setMainServerID(config.getLong("discord.mainServerID", getMainServerID()));
    }

    @Override
    public void save(Map<String, String> placeholders) {
        DiscomcConfig.put(placeholders, "discord.botToken", getToken());
        DiscomcConfig.put(placeholders, "discord.mainServerID", getMainServerID());
    }
}
