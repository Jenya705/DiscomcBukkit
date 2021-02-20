package space.cubicworld.connect;

import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;

@Data
public class ConnectConfiguration {

    private int codeRemovingTime = 6000;
    private int maxCodeValue = 10000;
    private int discordMessageDeleteTime = 5;

    public ConnectConfiguration(FileConfiguration config){
        setCodeRemovingTime(config.getInt("connect.codeRemovingTime"));
        setMaxCodeValue(config.getInt("connect.maxCodeValue"));
        setDiscordMessageDeleteTime(config.getInt("connect.discordMessageDeleteTime"));
    }

}
