package space.cubicworld.multichat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

@Data
public class MultiChatConfiguration {

    private String minecraftMessagePattern;

    public MultiChatConfiguration(FileConfiguration config){
        setMinecraftMessagePattern(config.getString("multiChat.minecraftMessagePattern"));
    }

}
