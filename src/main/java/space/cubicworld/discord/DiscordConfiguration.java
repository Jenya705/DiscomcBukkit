package space.cubicworld.discord;

import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;

@Data
public class DiscordConfiguration {

    private String token;
    private long mainServerID;

    public DiscordConfiguration(FileConfiguration config){
        setToken(config.getString("discord.botToken"));
        setMainServerID(config.getLong("discord.mainServerID"));
    }

}
